package net.phenix.discord.bot.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;

import com.google.api.services.sheets.v4.model.ValueRange;

import net.phenix.discord.bot.data.PetWrap;
import net.phenix.discord.bot.data.Skill;
import net.phenix.discord.bot.data.UnitWrap;
import net.phenix.discord.bot.data.xml.PetList.Pet;
import net.phenix.discord.bot.data.xml.TreasureList.Treasure;
import net.phenix.discord.bot.data.xml.TreasureSetList.TreasureSet;
import net.phenix.discord.bot.data.xml.UnitList.Unit;

public class BuildManager {

	Logger log = Logger.getLogger(getClass());

	private BundleManager bundleManager;

	private UnitManager unitManager;

	private PetManager petManager;

	private TreasureManager treasureManager;

	public static BuildManager getInstance() {
		return new BuildManager();
	}

	public void init(BundleManager bundleManager, UnitManager unitManager, PetManager petManager, TreasureManager treasureManager) {
		this.bundleManager = bundleManager;
		this.unitManager = unitManager;
		this.petManager = petManager;
		this.treasureManager = treasureManager;
	}

	public void build(List<Skill> result, List<Skill> resultPets, List<PetWrap> pets, List<UnitWrap> unitsTimeShop, List<UnitWrap> unitsTeamRevive, SheetManager sheetManager,
			TreasureManager treasureManager, PetManager petManager, String spreadsheetId) throws IOException {

		/** UNIT TIME SHOP */
		ValueRange response = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Unit!A2:F251").execute();
		List<List<Object>> values = response.getValues();
		if (values != null) {
			for (List<Object> row : values) {
				if (row.size() > 1) {
					String id = (String) row.get(0);
					Integer countT0 = 0;
					Integer countT1 = 0;
					Integer countT2 = 0;
					Integer countT3 = 0;
					if (row.size() > 2 && !row.get(2).toString().isEmpty())
						countT0 = Integer.parseInt((String) row.get(2));

					if (row.size() > 3 && !row.get(3).toString().isEmpty())
						countT1 = Integer.parseInt((String) row.get(3));

					if (row.size() > 4 && !row.get(4).toString().isEmpty())
						countT2 = Integer.parseInt((String) row.get(4));

					if (row.size() > 5 && !row.get(5).toString().isEmpty())
						countT3 = Integer.parseInt((String) row.get(5));

					UnitWrap unit = new UnitWrap(id, countT0, countT1, countT2, countT3);
					unitsTimeShop.add(unit);
				}
			}
		}

		/** UNIT TEAM REVIVE */
		response = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Autre!B16:D27").execute();
		values = response.getValues();
		if (values != null) {
			for (List<Object> row : values) {
				boolean isPresent = false;
				if (row.size() == 3) {
					String id = (String) row.get(0);
					String trans = (String) row.get(2);
					Integer countT0 = 0;
					Integer countT1 = 0;
					Integer countT2 = 0;
					Integer countT3 = 0;

					UnitWrap unit = new UnitWrap(id, countT0, countT1, countT2, countT3);
					if (unitsTeamRevive.contains(unit)) {
						int index = unitsTeamRevive.indexOf(unit);
						unit = unitsTeamRevive.get(index);
						isPresent = true;
					}

					if (trans.equals("0")) {
						countT0 = unit.getCountT0();
						countT0++;
						unit.setCountT0(countT0);
					} else if (trans.equals("1")) {
						countT1 = unit.getCountT1();
						countT1++;
						unit.setCountT1(countT1);
					} else if (trans.equals("2")) {
						countT2 = unit.getCountT2();
						countT2++;
						unit.setCountT2(countT2);
					} else if (trans.equals("3")) {
						countT3 = unit.getCountT3();
						countT3++;
						unit.setCountT3(countT3);
					}

					if (!isPresent) {
						unitsTeamRevive.add(unit);
					}
				}
			}
		}

		/** ARTEFACT */
		response = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Artefact!A2:D32").execute();
		values = response.getValues();
		Map<TreasureSet, Map<String, Integer>> sets = new HashMap<>();
		if (values != null) {
			for (List<Object> row : values) {

				if (row.size() == 4) {
					String id = (String) row.get(0);
					String trans = (String) row.get(2);
					String level = (String) row.get(3);
					Treasure treasure = treasureManager.getTreasureById(id);

					if (treasure != null) {
						String type1 = "";
						String type2 = "";
						if (trans.equals("0")) {
							type1 = treasure.getAbility1();
							type2 = treasure.getAbility2();
						} else if (trans.equals("1")) {
							type1 = treasure.getAbility11();
							type2 = treasure.getAbility12();
						} else if (trans.equals("2")) {
							type1 = treasure.getAbility21();
							type2 = treasure.getAbility22();
						}

						String value1 = treasureManager.getAbilityValue(type1, level);
						String value2 = treasureManager.getAbilityValue(type2, level);

						Skill skill = new Skill();
						skill.setCode(treasure.getSkillCode1());

						if (result.contains(skill)) {
							int index = result.indexOf(skill);
							BigDecimal value = result.get(index).getBonusArtefact();
							BigDecimal value_ = value.add(new BigDecimal(value1));
							result.get(index).setBonusArtefact(value_);
						} else {
							skill.setBonusArtefact(new BigDecimal(value1));
							result.add(skill);
						}

						Skill skill2 = new Skill();
						skill2.setCode(treasure.getSkillCode2());
						if (result.contains(skill2)) {
							int index = result.indexOf(skill2);
							BigDecimal value = result.get(index).getBonusArtefact();
							value = value.add(new BigDecimal(value2));
							result.get(index).setBonusArtefact(value);
						} else {
							skill2.setBonusArtefact(new BigDecimal(value2));
							result.add(skill2);
						}

						/** SET */
						TreasureSet set = treasureManager.getSetByTreasureId(id);
						if (sets.containsKey(set)) {
							Map<String, Integer> countTrans = sets.get(set);
							if (countTrans.containsKey(trans)) {
								Integer value = countTrans.get(trans);
								value++;
								countTrans.put(trans, value);
								sets.put(set, countTrans);
							} else {
								countTrans = new HashMap<>();
								countTrans.put(trans, 1);
								sets.put(set, countTrans);
							}

						} else {
							Map<String, Integer> countTrans = new HashMap<>();
							countTrans.put(trans, 1);
							sets.put(set, countTrans);
						}
					}
				}
			}
		}

		/** SETS */
		for (TreasureSet set : sets.keySet()) {

			String numSetList = set.getNumSetList();
			List<String> list = Arrays.asList(numSetList.split("\\|"));

			Map<String, Integer> transCount = sets.get(set);
			for (String trans : transCount.keySet()) {

				Integer count = transCount.get(trans);
				if (list.contains(count.toString())) {
					Integer index = list.indexOf(count.toString());

					if (trans.equals("0")) {
						List<String> skilllist = Arrays.asList(set.getSkillList().split("\\|"));
						List<String> valuelist = Arrays.asList(set.getValueList().split("\\|"));
						for (int i = 0; i < index + 1; i++) {
							String[] skillSets = skilllist.get(i).split("#");
							String[] valueSets = valuelist.get(i).split("#");

							for (int j = 0; j < skillSets.length; j++) {
								Skill skill = new Skill();
								skill.setCode(skillSets[j]);

								if (result.contains(skill)) {
									int ind = result.indexOf(skill);
									BigDecimal value = result.get(ind).getBonusArtefact();
									BigDecimal value_ = value.add(new BigDecimal(valueSets[j]));
									result.get(ind).setBonusArtefact(value_);
								} else {
									skill.setBonusArtefact(new BigDecimal(valueSets[j]));
									result.add(skill);
								}
							}
						}
					} else if (trans.equals("1")) {
						List<String> skilllist = Arrays.asList(set.getSkillList().split("\\|"));
						List<String> valuelist = Arrays.asList(set.getValueList1().split("\\|"));
						for (int i = 0; i < index + 1; i++) {
							String[] skillSets = skilllist.get(i).split("#");
							String[] valueSets = valuelist.get(i).split("#");

							for (int j = 0; j < skillSets.length; j++) {
								Skill skill = new Skill();
								skill.setCode(skillSets[j]);

								if (result.contains(skill)) {
									int ind = result.indexOf(skill);
									BigDecimal value = result.get(ind).getBonusArtefact();
									BigDecimal value_ = value.add(new BigDecimal(valueSets[j]));
									result.get(ind).setBonusArtefact(value_);
								} else {
									skill.setBonusArtefact(new BigDecimal(valueSets[j]));
									result.add(skill);
								}
							}
						}
					} else if (trans.equals("2")) {
						List<String> skilllist = Arrays.asList(set.getSkillList().split("\\|"));
						List<String> valuelist = Arrays.asList(set.getValueList2().split("\\|"));
						for (int i = 0; i < index + 1; i++) {
							String[] skillSets = skilllist.get(i).split("#");
							String[] valueSets = valuelist.get(i).split("#");

							for (int j = 0; j < skillSets.length; j++) {
								Skill skill = new Skill();
								skill.setCode(skillSets[j]);

								if (result.contains(skill)) {
									int ind = result.indexOf(skill);
									BigDecimal value = result.get(ind).getBonusArtefact();
									BigDecimal value_ = value.add(new BigDecimal(valueSets[j]));
									result.get(ind).setBonusArtefact(value_);
								} else {
									skill.setBonusArtefact(new BigDecimal(valueSets[j]));
									result.add(skill);
								}
							}
						}
					}
				}
			}
		}

		/** PETS */

		//Pet Principale
		ValueRange responsePetPrinc = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Autre!B30:C30").execute();
		List<List<Object>> valuesPetPrinc = responsePetPrinc.getValues();
		String petPrincipaleId = null;
		
		if (valuesPetPrinc != null) {
			for (List<Object> row : valuesPetPrinc) {
				if (row.size() == 2) {
					petPrincipaleId = (String) row.get(1);
				}
			}
		}

		//Pet ressurection
		ValueRange responsePetRessu = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Autre!B31:C31").execute();
		List<List<Object>> valuesPetResu = responsePetRessu.getValues();
		String petResuId = null;
		
		if (valuesPetResu != null) {
			for (List<Object> row : valuesPetResu) {
				if (row.size() == 2) {
					petResuId = (String) row.get(1);
				}
			}
		}
		
		response = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Pets!A2:D250").execute();
		values = response.getValues();

		if (values != null) {
			// Map<String, BigDecimal> stats = new HashMap<String,
			// BigDecimal>();
			for (List<Object> row : values) {

				if (row.size() == 4) {
					String id = (String) row.get(0);
					String level = (String) row.get(2);
					String fragment = (String) row.get(3);
					Pet pet = petManager.getPetById(id);

					if (pet != null) {

						setPets(resultPets, pet.getSkill1(), petManager.getAbilityValue(pet.getValue1(), level));
						setPets(resultPets, pet.getSkill2(), petManager.getAbilityValue(pet.getValue2(), level));
						setPets(resultPets, pet.getMasterSkill(), petManager.getAbilityValue(pet.getValue3(), level));

						PetWrap petWrap = new PetWrap(id, level, fragment);
						if(id.equals(petPrincipaleId)){
							petWrap.setPrincipale(true);
						}
						if(id.equals(petResuId)){
							petWrap.setResu(true);
						}
						pets.add(petWrap);

					}
				}
			}
		}
	}

	public void setPets(List<Skill> result, String skillCode, String value) {
		Skill skill = new Skill();
		skill.setCode(skillCode);
		if (result.contains(skill)) {
			int index = result.indexOf(skill);
			BigDecimal oldValue = result.get(index).getBonusPet();
			oldValue = oldValue.add(new BigDecimal(value));
			result.get(index).setBonusPet(oldValue);
		} else {
			skill.setBonusPet(new BigDecimal(value));
			result.add(skill);
		}
	}

	public BundleManager getBundleManager() {
		return bundleManager;
	}

	public void setBundleManager(BundleManager bundleManager) {
		this.bundleManager = bundleManager;
	}

	public String compute(List<Skill> skillArtes, List<Skill> skillPet, List<PetWrap> pets, List<UnitWrap> unitsTimeShop, List<UnitWrap> unitsTeamRevive) {

		String result = "<table style='background: #424242;'>";

		result += getSkillHtml("questGoldGain_A", "1", "Augmente l'or obtenu en achevant des quêtes", skillArtes, skillPet);
		result += getSkillHtml("questUpgradeGold_A", "410", "Diminue le coût d'amélioration des quêtes", skillArtes, skillPet);
		result += getSkillHtml("unitUpgradeGold_A", "400", "Diminue le coût d'amélioration des unités", skillArtes, skillPet);
		result += getSkillHtml("questOpenGold_A", null, "Diminue le coût d'ouverture de quête", skillArtes, skillPet);
		result += getSkillHtml("questCompleteTime_A", null, "Réduction de la durée de quête", skillArtes, skillPet);
		result += getSkillHtmlReviveMedal("reviveMedal", "283", "Augmente la quantité de médailles", skillArtes, skillPet, pets, unitsTimeShop, unitsTeamRevive);

		result += "</table>";

		return result;
	}

	private String getSkillHtmlReviveMedal(String arteSkillCode, String petId, String label, List<Skill> skillArtes, List<Skill> skillPet, List<PetWrap> pets, List<UnitWrap> unitsTimeShop,
			List<UnitWrap> unitsTeamRevive) {

		Skill arteSkill = getArteSkillByCode(skillArtes, arteSkillCode);
		BigDecimal valueArte = new BigDecimal(0);
		BigDecimal total = new BigDecimal(0);
		if (arteSkill != null) {
			valueArte = arteSkill.getBonusArtefact();
			total = arteSkill.getBonusArtefact();
		}

		BigDecimal valuePet = new BigDecimal(0);
		Skill petSkill = getPetSkillById(skillPet, petId);
		if (petSkill != null) {
			valuePet = petSkill.getBonusPet();
		}

		BigDecimal valueUnit = new BigDecimal(0);
		BigDecimal valueHumanUnit = new BigDecimal(0);
		BigDecimal valueElfUnit = new BigDecimal(0);
		BigDecimal valueUndeadUnit = new BigDecimal(0);
		BigDecimal valueOrcUnit = new BigDecimal(0);

		for (UnitWrap unitWrap : unitsTimeShop) {
			Unit unit = unitManager.getUnitByCode(unitWrap.getCode());
			if (unit != null) {
				Double valueT1 = 0.0;
				Double valueT2 = 0.0;
				Double valueT3 = 0.0;
				if (unit.getRank().equals("2")) {
					valueT1 = 0.0;
					valueT2 = 0.5;
					valueT3 = 0.5;
				} else if (unit.getRank().equals("3")) {
					valueT1 = 0.5;
					valueT2 = 1.0;
					valueT3 = 1.5;
				} else if (unit.getRank().equals("4")) {
					valueT1 = 1.5;
					valueT2 = 2.5;
					valueT3 = 5.0;
				} else if (unit.getRank().equals("5")) {
					valueT1 = 2.5;
					valueT2 = 5.0;
					valueT3 = 7.5;
				} else if (unit.getRank().equals("6")) {
					valueT1 = 5.0;
					valueT2 = 10.0;
					valueT3 = 15.0;
				}
				BigDecimal t1 = new BigDecimal(unitWrap.getCountT1()).multiply(new BigDecimal(valueT1));
				BigDecimal t2 = new BigDecimal(unitWrap.getCountT2()).multiply(new BigDecimal(valueT2));
				BigDecimal t3 = new BigDecimal(unitWrap.getCountT3()).multiply(new BigDecimal(valueT3));

				BigDecimal bonus = t1.add(t2).add(t3);

				Pet pet = petManager.getPetByUnitId(unitWrap.getCode());
				if (pet != null) {
					// pet 5* bonus +50%
					PetWrap petWrap = getPetWrap(pets, pet.getKindNum());
					if (petWrap != null) {
						if (petWrap.getLevel().equals("5")) {
							bonus = bonus.multiply(new BigDecimal(1.5));
						}
					}
					// guardian bonus depends on level
					if (pet.getTribe().equals("1")) {
						Skill petHSkill = getPetSkillById(skillPet, "414");

						BigDecimal petValue = petHSkill.getBonusPet().divide(new BigDecimal(100)); // percent
						petValue = petValue.add(new BigDecimal(1));

						bonus = bonus.multiply(petValue);

						valueHumanUnit = valueHumanUnit.add(bonus);

					} else if (pet.getTribe().equals("2")) {
						Skill petESkill = getPetSkillById(skillPet, "415");
						BigDecimal petValue = petESkill.getBonusPet().divide(new BigDecimal(100)); // percent
						petValue = petValue.add(new BigDecimal(1));

						bonus = bonus.multiply(petValue);

						valueElfUnit = valueElfUnit.add(bonus);

					} else if (pet.getTribe().equals("3")) {
						Skill petUSkill = getPetSkillById(skillPet, "416");
						BigDecimal petValue = petUSkill.getBonusPet().divide(new BigDecimal(100)); // percent
						petValue = petValue.add(new BigDecimal(1));

						bonus = bonus.multiply(petValue);
						valueUndeadUnit = valueUndeadUnit.add(bonus);

					} else if (pet.getTribe().equals("4")) {
						Skill petOSkill = getPetSkillById(skillPet, "417");
						BigDecimal petValue = petOSkill.getBonusPet().divide(new BigDecimal(100)); // percent
						petValue = petValue.add(new BigDecimal(1));

						bonus = bonus.multiply(petValue);
						valueOrcUnit = valueOrcUnit.add(bonus);
					}
				}

				valueUnit = valueUnit.add(bonus);
			}
		}

		total = valueArte.add(valuePet).add(valueUnit);

		//@formatter:off
		String result = "<th style='color: #ddaa2a;font-weight: bold' colspan='4'>" + label + "</th>" + 
			"<tr style='color: #ddaa2a;font-weight: bold'>" + 
				"<td>Unité</td>" + 
				"<td>Artéfact</td>" +
				"<td>Pet</td>" + 
				"<td>Total</td>" + 
			"</tr>" + 
			"<tr style='color: #dcdcdc;'>" +
				"<td>" + valueUnit + " %</td>" + 
				"<td>" + valueArte + " %</td>" + 
				"<td>" + valuePet + " %</td>" + 
				"<td>" + total	+ " %</td>" + 
			"</tr>" + 
			"<tr style='color: #ddaa2a;font-weight: bold'>" + 
				"<td>Dont gardien : Humain</td>" + 
				"<td>Elfe</td>" + 
				"<td>Orc</td>" + 
				"<td>Mort Vivant</td>" + 
			"</tr>"
			+ "<tr style='color: #dcdcdc;'>" +
				"<td>" + valueHumanUnit + " %</td>" + 
				"<td>" + valueElfUnit + " %</td>" + 
				"<td>" + valueOrcUnit + " %</td>" + 
				"<td>" + valueUndeadUnit + " %</td>" + 
			"</tr>";

		//@formatter:on
		skillArtes.remove(arteSkill);

		return result;
	}

	private PetWrap getPetWrap(List<PetWrap> pets, String kindNum) {

		for (PetWrap petWrap : pets) {
			if (petWrap.getId().equals(kindNum)) {
				return petWrap;
			}
		}
		return null;
	}

	private String getSkillHtml(String arteSkillCode, String petId, String label, List<Skill> skillArtes, List<Skill> skillPet) {
		Skill arteSkill = getArteSkillByCode(skillArtes, arteSkillCode);
		BigDecimal valueArte = new BigDecimal(0);
		BigDecimal total = new BigDecimal(0);
		if (arteSkill != null) {
			valueArte = arteSkill.getBonusArtefact();
			total = arteSkill.getBonusArtefact();
		}

		BigDecimal valuePet = new BigDecimal(0);
		Skill petSkill = getPetSkillById(skillPet, petId);
		if (petSkill != null) {
			valuePet = petSkill.getBonusPet();
		}

		if (!valuePet.equals(new BigDecimal(0))) {
			total = valueArte.multiply(valuePet);
		}

		//@formatter:off
		String result = "<th style='color: #ddaa2a;font-weight: bold' colspan='4'>" + label +"</th>" + 
			"<tr style='color: #ddaa2a;font-weight: bold'>"
				+ "<td>Unité</td>"
				+ "<td>Artéfact</td>"
				+ "<td>Pet</td>"
				+ "<td>Total</td>"
			+ "</tr>"
			+ "<tr style='color: #dcdcdc;'><td/>"
				+ "<td>" + NumberManager.getEFNumber(valueArte) + "</td>"
				+ "<td>"+ NumberManager.getEFNumber(valuePet) + "</td"
				+ "><td>" + NumberManager.getEFNumber(total) + "</td>"
			+ "</tr>";

		//@formatter:on

		skillArtes.remove(arteSkill);

		return result;
	}

	private Skill getArteSkillByCode(List<Skill> skillArtes, String arteSkillCode) {
		for (Skill skill : skillArtes) {
			if (skill.getCode().equals(arteSkillCode)) {
				return skill;
			}
		}
		return null;
	}

	public Skill getPetSkillById(List<Skill> skillPet, String id) {

		for (Skill skill : skillPet) {
			if (skill.getCode().equals(id)) {
				return skill;
			}
		}
		return null;
	}

	public UnitManager getUnitManager() {
		return unitManager;
	}

	public void setUnitManager(UnitManager unitManager) {
		this.unitManager = unitManager;
	}

	public PetManager getPetManager() {
		return petManager;
	}

	public void setPetManager(PetManager petManager) {
		this.petManager = petManager;
	}

	public TreasureManager getTreasureManager() {
		return treasureManager;
	}

	public void setTreasureManager(TreasureManager treasureManager) {
		this.treasureManager = treasureManager;
	}

}
