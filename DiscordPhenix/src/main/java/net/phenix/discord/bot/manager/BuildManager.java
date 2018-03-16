package net.phenix.discord.bot.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.api.services.sheets.v4.model.ValueRange;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.phenix.discord.bot.data.ArtefactSetWrap;
import net.phenix.discord.bot.data.ArtefactWrap;
import net.phenix.discord.bot.data.PetWrap;
import net.phenix.discord.bot.data.Skill;
import net.phenix.discord.bot.data.UnitTeamWrap;
import net.phenix.discord.bot.data.UnitWrap;
import net.phenix.discord.bot.data.xml.PetList.Pet;
import net.phenix.discord.bot.data.xml.QuestList;
import net.phenix.discord.bot.data.xml.QuestList.Quest;
import net.phenix.discord.bot.data.xml.TreasureList.Treasure;
import net.phenix.discord.bot.data.xml.TreasureSetList.TreasureSet;
import net.phenix.discord.bot.data.xml.UnitList.Unit;
import net.phenix.discord.bot.manager.UnitManager.Tribe;

public class BuildManager extends AbstractManager{

	Logger log = Logger.getLogger(getClass());

	private BundleManager bundleManager;

	private UnitManager unitManager;

	private PetManager petManager;

	private TreasureManager treasureManager;

	private QuestList questList;

	public static BuildManager getInstance() {
		return new BuildManager();
	}

	public void init(BundleManager bundleManager, UnitManager unitManager, PetManager petManager, TreasureManager treasureManager) throws JAXBException {
		this.bundleManager = bundleManager;
		this.unitManager = unitManager;
		this.petManager = petManager;
		this.treasureManager = treasureManager;

		InputStream is = getClass().getResourceAsStream("/xml/questbook.xml");

		JAXBContext jaxbContext = JAXBContext.newInstance(QuestList.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		this.setQuestList((QuestList) jaxbUnmarshaller.unmarshal(is));

		log.info("QuestList : Init done ");
	}

	public void build(List<ArtefactWrap> resultArte, List<ArtefactSetWrap> resultArteSet, List<PetWrap> pets, List<UnitWrap> unitsTimeShop, List<UnitWrap> unitsTeamRevive,
			List<UnitTeamWrap> unitsTeams, SheetManager sheetManager, TreasureManager treasureManager, PetManager petManager, String spreadsheetId, Workbook workbook) throws IOException {

		List<List<Object>> values = null;
		ValueRange response = null;
		/** UNIT TIME SHOP */
		if (spreadsheetId != null) {
			response = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Unit!A2:F251").execute();
			values = response.getValues();
		} else {
			values = sheetManager.getValues(workbook.getSheet("Unit"), 0, 1, 5, 250);
		}
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
		if (spreadsheetId != null) {
			response = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Autre!B16:D27").execute();
			values = response.getValues();
		} else {
			values = sheetManager.getValues(workbook.getSheet("Autre"), 1, 15, 3, 26);
		}
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
			
			//remove unit in revive team to unit in time shop
			for (UnitWrap unitWrap : unitsTeamRevive) {
				for (UnitWrap unitWrapShop : unitsTimeShop) {
					if(unitWrap.equals(unitWrapShop)){
						unitWrapShop.setCountT0(unitWrapShop.getCountT0()-unitWrap.getCountT0());
						unitWrapShop.setCountT1(unitWrapShop.getCountT1()-unitWrap.getCountT1());
						unitWrapShop.setCountT2(unitWrapShop.getCountT2()-unitWrap.getCountT2());
						unitWrapShop.setCountT3(unitWrapShop.getCountT3()-unitWrap.getCountT3());
					}
				}
			}
		}

		/** UNIT TEAM */
		if (spreadsheetId != null) {
			response = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Autre!B2:F13").execute();
			values = response.getValues();
		} else {
			values = sheetManager.getValues(workbook.getSheet("Autre"), 1, 1, 5, 12);
		}
		if (values != null) {
			for (List<Object> row : values) {
				if (row.size() == 5) {
					String id = (String) row.get(0);
					Integer trans = Integer.parseInt((String) row.get(2));
					Integer goldLevel = Integer.parseInt((String) row.get(3));
					Integer medal = Integer.parseInt((String) row.get(4));

					UnitTeamWrap unit = new UnitTeamWrap(id, trans, goldLevel, medal);
					unitsTeams.add(unit);
				}
			}
		}

		/** ARTEFACT */
		if (spreadsheetId != null) {
			response = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Autre!A2:D32").execute();
			values = response.getValues();
		} else {
			values = sheetManager.getValues(workbook.getSheet("Artefact"), 0, 1, 3, 31);
		}
		if (values != null) {
			for (List<Object> row : values) {

				if (row.size() == 4) {
					String id = (String) row.get(0);
					String trans = (String) row.get(2);
					String level = (String) row.get(3);

					ArtefactWrap arteWrap = new ArtefactWrap(id, trans, level);
					resultArte.add(arteWrap);

					/** SET */
					TreasureSet set = treasureManager.getSetByTreasureId(id);

					ArtefactSetWrap setWrap = new ArtefactSetWrap();
					setWrap.setId(set.getKindNum());
					if (resultArteSet.contains(setWrap)) {
						Integer index = resultArteSet.indexOf(setWrap);
						Integer tmp = resultArteSet.get(index).getLevel();
						tmp++;
						resultArteSet.get(index).setLevel(tmp);

					} else {
						setWrap.setTrans(trans);
						setWrap.setLevel(1);
						resultArteSet.add(setWrap);
					}
				}
			}
		}

		/** PETS */

		// Pet Principale
		ValueRange responsePetPrinc = null;
		List<List<Object>> valuesPetPrinc = null;
		if (spreadsheetId != null) {
			responsePetPrinc = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Autre!B30:C30").execute();
			valuesPetPrinc = responsePetPrinc.getValues();
		} else {
			valuesPetPrinc = sheetManager.getValues(workbook.getSheet("Autre"), 1, 29, 2, 29);
		}
		String petPrincipaleId = null;

		if (valuesPetPrinc != null) {
			for (List<Object> row : valuesPetPrinc) {
				if (row.size() == 2) {
					petPrincipaleId = (String) row.get(0);
				}
			}
		}

		// Pet ressurection
		ValueRange responsePetRessu = null;
		List<List<Object>> valuesPetResu = null;
		if (spreadsheetId != null) {
			responsePetRessu = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Autre!B31:C31").execute();
			valuesPetResu = responsePetRessu.getValues();
		} else {
			valuesPetResu = sheetManager.getValues(workbook.getSheet("Autre"), 1, 30, 2, 30);
		}
		String petResuId = null;

		if (valuesPetResu != null) {
			for (List<Object> row : valuesPetResu) {
				if (row.size() == 2) {
					petResuId = (String) row.get(0);
				}
			}
		}


		if (spreadsheetId != null) {
			response = sheetManager.getService().spreadsheets().values().get(spreadsheetId, "Pets!A2:D25").execute();
			values = responsePetPrinc.getValues();
		} else {
			values = sheetManager.getValues(workbook.getSheet("Pets"), 0, 1, 3, 249);
		}
		
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

						PetWrap petWrap = new PetWrap(id, level, fragment);
						if (id.equals(petPrincipaleId)) {
							petWrap.setPrincipale(true);
						}
						if (id.equals(petResuId)) {
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

	public String compute(List<ArtefactWrap> arte, List<ArtefactSetWrap> arteSet, List<PetWrap> pets, List<UnitWrap> unitsTimeShop, List<UnitWrap> unitsTeamRevive) throws XPathExpressionException {

		String result = "<table style='background: #424242;'>";

		Skill unitUpdate = getSkillTotalValue("unitUpgradeGold_A", "400", arte, arteSet, pets);
		
		result += getSkillHtml("Diminue le coût d'amélioration des unités", arte, arteSet, pets, unitUpdate);

		result += "</table>";
		return result;
	}

	public String computeMedal(List<ArtefactWrap> arte, List<ArtefactSetWrap> arteSet, List<PetWrap> pets, List<UnitWrap> unitsTimeShop, List<UnitWrap> unitsTeamRevive) throws XPathExpressionException {

		String result = "<table style='background: #424242;'>";

		result += getSkillHtmlReviveMedal("reviveMedal", "283", "Augmente la quantité de médailles", arte, arteSet, pets, unitsTimeShop, unitsTeamRevive);

		result += "</table>";
		return result;
	}
	
	public String computeUnit(List<ArtefactWrap> arte, List<ArtefactSetWrap> arteSet, List<PetWrap> pets, List<UnitWrap> unitsTimeShop, List<UnitWrap> unitsTeamRevive, List<UnitTeamWrap> unitsTeams) {

		String result = "";
		Collections.sort(unitsTeams);
		if (unitsTeams.size() > 1) {
			Unit core1 = unitManager.getUnitByCode(unitsTeams.get(0).getCode());

			String att = "";
			String hp = "";
			String md = "";
			String pd = "";

			Skill attackDamage = new Skill();
			Skill health = new Skill();
			Skill magicDefense = new Skill();
			Skill physicalDefense = new Skill();

			// Passion Intérieur
			Integer ip = getIPValue(pets, unitsTimeShop);

			List<UnitWrap> unitRP = new ArrayList<>();
			List<UnitWrap> unitRPSenior = new ArrayList<>();

			Integer rp = 0;
			List<PetWrap> petRP = new ArrayList<>();
			if (core1.getTribe().equals(Tribe.HUMAN)) {
				attackDamage = getSkillTotalValue("damage_H", null, arte, arteSet, pets);
				health = getSkillTotalValue("hp_A", null, arte, arteSet, pets);
				magicDefense = getSkillTotalValue("defense_H", null, arte, arteSet, pets);
				physicalDefense = getSkillTotalValue("defense_H", null, arte, arteSet, pets);

				rp = getRPValue(unitRP, unitRPSenior, unitsTeams, unitsTimeShop, petRP, pets, "112", "166", "167", "168", "169", 10, 20, 4, 8);

				BigDecimal inc = new BigDecimal("1.05");
				inc = inc.pow(unitsTeams.get(0).getMedal() + unitsTeams.get(0).getGoldLevel() - 1 + ip + rp);

				att = getTotalValue(core1.getInitDamage(), attackDamage, inc);
				hp = getTotalValue(core1.getInitHp(), health, inc);
				md = getTotalValue(core1.getInitMagDef(), magicDefense, inc);
				pd = getTotalValue(core1.getInitPhyDef(), physicalDefense, inc);

			} else if (core1.getTribe().equals(Tribe.ELF)) {

				rp = getRPValue(unitRP, unitRPSenior, unitsTeams, unitsTimeShop, petRP, pets, "113", "168", "169", "170", "171", 7, 15, 5, 10);

			} else if (core1.getTribe().equals(Tribe.UNDEAD)) {

				rp = getRPValue(unitRP, unitRPSenior, unitsTeams, unitsTimeShop, petRP, pets, "114", "170", "171", "172", "173", 10, 20, 4, 8);

			} else if (core1.getTribe().equals(Tribe.ORC)) {

				rp = getRPValue(unitRP, unitRPSenior, unitsTeams, unitsTimeShop, petRP, pets, "115", "172", "173", "168", "169", 7, 15, 5, 10);
			}

			result += "attackDamage : " + att + "\n";
			result += "health : " + hp + "\n";
			result += "magicDefense : " + md + "\n";
			result += "physicalDefense : " + pd + "\n";
			result += "IP : " + ip + "\n";
			result += "Renforcement de tribu : " + rp + "\n";

			Unit core2 = unitManager.getUnitByCode(unitsTeams.get(1).getCode());

		}
		return result;
	}

	public String getTotalValue(String value, Skill skill, BigDecimal inc) {
		BigDecimal bonusArte = skill.getBonusArtefact();
		bonusArte = bonusArte.divide(new BigDecimal(100), 2, RoundingMode.CEILING);

		BigDecimal base = new BigDecimal(value).multiply(inc);

		return NumberManager.getEFNumber(base) + "(" + NumberManager.getEFNumber(base.multiply(bonusArte)) + ")       (" + NumberManager.getEFNumber(bonusArte) + ")";
	}

	public Integer getRPValue(List<UnitWrap> unitRP, List<UnitWrap> unitRPSenior, List<UnitTeamWrap> unitsTeams, List<UnitWrap> unitsTimeShop, List<PetWrap> petRP, List<PetWrap> pets, String petID,
			String unitID, String unitSeniorID, String unit2ID, String unitSenior2ID, Integer unitRatio, Integer unitSeniorRatio, Integer unit2Ratio, Integer unitSenior2Ratio) {

		Integer rpmax = 150;
		Integer rp = 0;
		petRP = pets.stream().filter(p -> p.getId().equals(petID)).collect(Collectors.toList());
		if (petRP.size() != 0 && petRP.get(0).getLevel().equals("5")) {
			rpmax += 100;
			unitRP = unitsTimeShop.stream().filter(u -> u.getCode().equals(unitID)).collect(Collectors.toList());
			unitRPSenior = unitsTimeShop.stream().filter(u -> u.getCode().equals(unitSeniorID)).collect(Collectors.toList());
			if (unitRP.size() != 0) {
				UnitWrap unit = unitRP.get(0);
				rp = rp + (unit.getCountT0() * unitRatio);
			}
			if (unitRPSenior.size() != 0) {
				UnitWrap unit = unitRPSenior.get(0);
				rp = rp + ((unit.getCountT0() + unit.getCountT1() + unit.getCountT2() + unit.getCountT3()) * unitSeniorRatio);
			}
		}

		Integer countUnit = unitsTeams.stream().filter(u -> u.getCode().equals(unitID)).collect(Collectors.toList()).size();
		Integer countUnitSenior = unitsTeams.stream().filter(u -> u.getCode().equals(unitSeniorID)).collect(Collectors.toList()).size();

		Integer countUnit2 = unitsTeams.stream().filter(u -> u.getCode().equals(unit2ID)).collect(Collectors.toList()).size();
		Integer countUnit2Senior = unitsTeams.stream().filter(u -> u.getCode().equals(unitSenior2ID)).collect(Collectors.toList()).size();

		rp = rp + (countUnit * unitRatio);
		rp = rp + (countUnitSenior * unitSeniorRatio);
		rp = rp + (countUnit2 * unit2Ratio);
		rp = rp + (countUnit2Senior * unitSenior2Ratio);

		if (rp > rpmax) {
			rp = rpmax;
		}

		return rp;
	}

	public Integer getIPValue(List<PetWrap> pets, List<UnitWrap> unitsTimeShop) {
		Integer ipmax = 100;
		Integer ip = 0;
		List<PetWrap> E77 = pets.stream().filter(p -> p.getId().equals("48")).collect(Collectors.toList());
		List<UnitWrap> steampunk = unitsTimeShop.stream().filter(u -> u.getCode().equals("134")).collect(Collectors.toList());
		List<UnitWrap> steampunkSenior = unitsTimeShop.stream().filter(u -> u.getCode().equals("135")).collect(Collectors.toList());
		if (E77.size() != 0 && E77.get(0).getLevel().equals("5")) {
			ipmax += 50;
		}

		if (steampunk.size() != 0) {
			UnitWrap unit = steampunk.get(0);
			ip = ip + (unit.getCountT0() * 2);
		}
		if (steampunkSenior.size() != 0) {
			UnitWrap unit = steampunkSenior.get(0);
			ip = ip + ((unit.getCountT0() + unit.getCountT1() + unit.getCountT2() + unit.getCountT3()) * 5);
		}

		List<PetWrap> windy = pets.stream().filter(p -> p.getId().equals("49")).collect(Collectors.toList());
		List<UnitWrap> sylphid = unitsTimeShop.stream().filter(u -> u.getCode().equals("136")).collect(Collectors.toList());
		List<UnitWrap> sylphidSenior = unitsTimeShop.stream().filter(u -> u.getCode().equals("137")).collect(Collectors.toList());
		if (windy.size() != 0 && windy.get(0).getLevel().equals("5")) {
			ipmax += 50;
		}

		if (sylphid.size() != 0) {
			UnitWrap unit = sylphid.get(0);
			ip = ip + (unit.getCountT0() * 2);
		}
		if (sylphidSenior.size() != 0) {
			UnitWrap unit = sylphidSenior.get(0);
			ip = ip + ((unit.getCountT0() + unit.getCountT1() + unit.getCountT2() + unit.getCountT3()) * 5);
		}

		List<PetWrap> darkSnake = pets.stream().filter(p -> p.getId().equals("50")).collect(Collectors.toList());
		List<UnitWrap> medusa = unitsTimeShop.stream().filter(u -> u.getCode().equals("138")).collect(Collectors.toList());
		List<UnitWrap> medusaSenior = unitsTimeShop.stream().filter(u -> u.getCode().equals("139")).collect(Collectors.toList());
		if (darkSnake.size() != 0 && darkSnake.get(0).getLevel().equals("5")) {
			ipmax += 50;
		}

		if (medusa.size() != 0) {
			UnitWrap unit = medusa.get(0);
			ip = ip + (unit.getCountT0() * 2);
		}
		if (medusaSenior.size() != 0) {
			UnitWrap unit = medusaSenior.get(0);
			ip = ip + ((unit.getCountT0() + unit.getCountT1() + unit.getCountT2() + unit.getCountT3()) * 5);
		}

		List<PetWrap> seahorse = pets.stream().filter(p -> p.getId().equals("51")).collect(Collectors.toList());
		List<UnitWrap> naga = unitsTimeShop.stream().filter(u -> u.getCode().equals("140")).collect(Collectors.toList());
		List<UnitWrap> nagaSenior = unitsTimeShop.stream().filter(u -> u.getCode().equals("141")).collect(Collectors.toList());
		if (seahorse.size() != 0 && seahorse.get(0).getLevel().equals("5")) {
			ipmax += 50;
		}

		if (naga.size() != 0) {
			UnitWrap unit = naga.get(0);
			ip = ip + (unit.getCountT0() * 2);
		}
		if (nagaSenior.size() != 0) {
			UnitWrap unit = nagaSenior.get(0);
			ip = ip + ((unit.getCountT0() + unit.getCountT1() + unit.getCountT2() + unit.getCountT3()) * 5);
		}

		if (ip > ipmax) {
			ip = ipmax;
		}
		return ip;
	}

	public String computeQuest(List<ArtefactWrap> arte, List<ArtefactSetWrap> arteSet, List<PetWrap> pets, List<UnitWrap> unitsTimeShop, List<UnitWrap> unitsTeamRevive)
			throws XPathExpressionException {

		Skill openGold = getSkillTotalValue("questOpenGold_A", null, arte, arteSet, pets);
		Skill completeTime = getSkillTotalValue("questCompleteTime_A", null, arte, arteSet, pets);
		Skill upgradeGold = getSkillTotalValue("questUpgradeGold_A", "410", arte, arteSet, pets);
		Skill goldGain = getSkillTotalValue("questGoldGain_A", "1", arte, arteSet, pets);
		Skill increaseMaxQuestlevel = getSkillTotalValue(null, "409", arte, arteSet, pets);

		String result = "<table style='background: #424242;'>";
		result += getSkillHtml("Diminue le coût d'ouverture de quête", arte, arteSet, pets, openGold);
		result += getSkillHtml("Réduction de la durée de quête", arte, arteSet, pets, completeTime);
		result += getSkillHtml("Diminue le coût d'amélioration des quêtes", arte, arteSet, pets, upgradeGold);
		result += getSkillHtml("Augmente l'or obtenu en achevant des quêtes", arte, arteSet, pets, goldGain);
		result += getSkillHtmlQuest(openGold, completeTime, upgradeGold, goldGain, increaseMaxQuestlevel);
		result += "</table>";
		return result;
	}

	private String getSkillHtmlQuest(Skill skillOpenGold, Skill skillCompleteTime, Skill skillUpgradeGold, Skill skillGoldGain, Skill increaseMaxQuestlevel) throws XPathExpressionException {

		BigDecimal baseMaxQuest = NumberManager.getNumber("2c");
		baseMaxQuest = baseMaxQuest.add(baseMaxQuest.multiply(increaseMaxQuestlevel.getBonusPet().divide(new BigDecimal(100))));
		
		String result = "";
		//@formatter:on
		boolean first = true;
		BigDecimal openGold = skillOpenGold.getTotal();
		BigDecimal completeTime = skillCompleteTime.getTotal();
		
		BigDecimal goldGain = skillGoldGain.getTotal();

		openGold = openGold.divide(new BigDecimal(100), 2, RoundingMode.CEILING);
		completeTime = completeTime.divide(new BigDecimal(100), 2, RoundingMode.CEILING);
		goldGain = goldGain.divide(new BigDecimal(100), 2, RoundingMode.CEILING);
		for (Quest quest : questList.getQuests()) {
			BigDecimal openPrice = new BigDecimal(quest.getOpenGold());
			if (openGold.compareTo(BigDecimal.ZERO) != 0) {
				openPrice = openPrice.divide(openGold, 2, RoundingMode.CEILING);
			}

			BigDecimal time = new BigDecimal(quest.getDuration().substring(0, quest.getDuration().length() - 1));
			if (completeTime.compareTo(BigDecimal.ZERO) != 0) {
				time = time.divide(completeTime, 2, RoundingMode.CEILING);
			}

			BigDecimal upgradePrice = new BigDecimal(quest.getUpgradeGold());
			BigDecimal upgradeGoldArte = skillUpgradeGold.getBonusArtefact();
			if (upgradeGoldArte.compareTo(BigDecimal.ZERO) != 0) {
				upgradeGoldArte = upgradeGoldArte.divide(new BigDecimal(100), 2, RoundingMode.CEILING);
				upgradePrice = upgradePrice.divide(upgradeGoldArte, 2, RoundingMode.CEILING);
			}
			BigDecimal upgradeGoldPet = skillUpgradeGold.getBonusPet();
			if (upgradeGoldPet.compareTo(BigDecimal.ZERO) != 0) {
				upgradeGoldPet = upgradeGoldPet.divide(new BigDecimal(100), 2, RoundingMode.CEILING);
				upgradePrice = upgradePrice.divide(upgradeGoldPet, 2, RoundingMode.CEILING);
			}
			
			BigDecimal upgradeTotalPrice = new BigDecimal(quest.getUpgradeGold());
			
			BigDecimal buff = skillUpgradeGold.getTotal().divide(new BigDecimal(100),2, RoundingMode.CEILING);
			BigDecimal gap = baseMaxQuest.multiply(baseMaxQuest).divide(new BigDecimal(2),2, RoundingMode.CEILING);
			upgradeTotalPrice = upgradeTotalPrice.divide(buff,2, RoundingMode.CEILING);
			upgradeTotalPrice = upgradeTotalPrice.multiply(gap);
			
			BigDecimal incGold = new BigDecimal(quest.getIncGold());
			if (goldGain.compareTo(BigDecimal.ZERO) != 0) {
				incGold = incGold.multiply(goldGain);
			}
			
			BigDecimal initGold = new BigDecimal(quest.getInitGold());
			initGold = initGold.add((new BigDecimal(quest.getIncGold())).multiply(baseMaxQuest));
			if (goldGain.compareTo(BigDecimal.ZERO) != 0) {
				initGold = initGold.multiply(goldGain);
			}
			//@formatter:off
			if(first){
			result += "<tr style='color: #ddaa2a;font-weight: bold'>"
					+ "<td></td>"
					+ "<td>Nom</td>"
					+ "<td>Cout d'ouverture</td>"
					+ "<td>Prix de départ</td>"
					+ "<td>Durée</td>"
					+ "<td>Cout d'amélioration</td>"
					+ "<td>Cout total d'amélioration</td>"
					+ "<td>Level max</td>"
					+ "<td>Gain d'or au lvl max</td>"					
					+ "<td></td>"
				+ "</tr>";
				first = false ;
			}
			
			
			String lang = ConfigManager.getConfig(event).getLang();
			
			
			result += "<tr style='color: #dcdcdc;'>"
					+ "<td/>"
					+ "<td>" + bundleManager.getBundle("/main/textList/text[id='QUEST_NAME_" + quest.getKindNum() + "']/value",lang) + "</td>"
					+ "<td>"+ NumberManager.getEFNumber(openPrice) + "</td"
					+ "<td>"+ NumberManager.getEFNumber(incGold) + "</td"
					+ "<td>"+ time + "s</td"
					+ "<td>"+ NumberManager.getEFNumber(upgradePrice) + "</td"
					+ "<td>"+ NumberManager.getEFNumber(upgradeTotalPrice) + "</td"
					+ "<td>"+ NumberManager.getEFNumber(baseMaxQuest) + "</td"
					+ "<td>"+ NumberManager.getEFNumber(initGold) + "</td"
					+ "><td></td>"
				+ "</tr>";
			//@formatter:on
		}
		return result;
	}

	private Skill getSkillTotalValue(String skillArteCode, String skillPetId, List<ArtefactWrap> arte, List<ArtefactSetWrap> arteSet, List<PetWrap> pets) {

		BigDecimal total = BigDecimal.ZERO;
		Skill skill = new Skill();
		BigDecimal valueArte = getSkillValueByCode(arte, skillArteCode);
		BigDecimal valueArteSet = getSkillSetValueByCode(arteSet, skillArteCode);
		valueArte = valueArte.add(valueArteSet);

		BigDecimal valuePet = getPetSkillValueById(pets, skillPetId, false);

		if (valuePet.compareTo(BigDecimal.ZERO) != 0) {
			total = valueArte.divide(new BigDecimal(100), 2, RoundingMode.CEILING).multiply(valuePet.divide(new BigDecimal(100), 2, RoundingMode.CEILING));
			total = total.multiply(new BigDecimal(100));
		} else {
			total = valueArte;
		}
		skill.setBonusArtefact(valueArte);
		skill.setBonusPet(valuePet);
		skill.setTotal(total);
		return skill;
	}

	private String getSkillHtml(String label, List<ArtefactWrap> arte, List<ArtefactSetWrap> arteSet, List<PetWrap> pets, Skill skill)
			throws XPathExpressionException {

		//@formatter:off
		String result = "<th style='color: #ddaa2a;font-weight: bold' colspan='4'>" + label +"</th>" + 
			"<tr style='color: #ddaa2a;font-weight: bold'>"
				+ "<td>Unité</td>"
				+ "<td>Artéfact</td>"
				+ "<td>Pet</td>"
				+ "<td>Total</td>"
			+ "</tr>"
			+ "<tr style='color: #dcdcdc;'><td/>"
				+ "<td>" + NumberManager.getEFNumber(skill.getBonusArtefact()) + "</td>"
				+ "<td>"+ NumberManager.getEFNumber(skill.getBonusPet()) + "</td"
				+ "><td>" + NumberManager.getEFNumber(skill.getTotal()) + "</td>"
			+ "</tr>";

		//@formatter:on

		return result;
	}

	private BigDecimal getPetSkillValueById(List<PetWrap> pets, String skillPetId, boolean isMedal) {
		BigDecimal result = BigDecimal.ZERO;

		if(skillPetId == null){
			return result;
		}
		for (PetWrap petWrap : pets) {
			Pet pet = petManager.getPetById(petWrap.getId());
			String value1 = "0";
			String value2 = "0";
			String value3 = "0";
			boolean found = false;
			if (pet.getSkill1().equals(skillPetId)) {
				value1 = petManager.getAbilityValue(pet.getValue1(), petWrap.getLevel());
				found = true;
			}
			if (pet.getSkill2().equals(skillPetId)) {
				value2 = petManager.getAbilityValue(pet.getValue2(), petWrap.getLevel());
				found = true;
			}
			if (pet.getMasterSkill().equals(skillPetId)) {
				value3 = petManager.getAbilityValue(pet.getValue3(), petWrap.getLevel());
				found = true;
			}

			if (found) {
				if (isMedal) {
					if (petWrap.isResu()) {
						result = result.add(new BigDecimal(value1).multiply(new BigDecimal(2)));
						result = result.add(new BigDecimal(value2).multiply(new BigDecimal(2)));
						result = result.add(new BigDecimal(value3).multiply(new BigDecimal(2)));
					} else {
						result = result.add(new BigDecimal(value1));
						result = result.add(new BigDecimal(value2));
						result = result.add(new BigDecimal(value3));
					}
				} else {
					if (petWrap.isPrincipale()) {
						result = result.add(new BigDecimal(value1).multiply(new BigDecimal(2)));
						result = result.add(new BigDecimal(value2).multiply(new BigDecimal(2)));
						result = result.add(new BigDecimal(value3).multiply(new BigDecimal(2)));
					} else {
						result = result.add(new BigDecimal(value1));
						result = result.add(new BigDecimal(value2));
						result = result.add(new BigDecimal(value3));
					}
				}
			}
		}
		return result;
	}

	private String getSkillHtmlReviveMedal(String skillArteCode, String skillPetId, String label, List<ArtefactWrap> arte, List<ArtefactSetWrap> arteSet, List<PetWrap> pets,
			List<UnitWrap> unitsTimeShop, List<UnitWrap> unitsTeamRevive) {

		BigDecimal total = BigDecimal.ZERO;

		BigDecimal valueArte = getSkillValueByCode(arte, skillArteCode);
		BigDecimal valueArteSet = getSkillSetValueByCode(arteSet, skillArteCode);
		valueArte = valueArte.add(valueArteSet);

		BigDecimal valuePet = getPetSkillValueById(pets, skillPetId, true);

		BigDecimal valueUnit = BigDecimal.ZERO;
		BigDecimal valueUnitRevive = BigDecimal.ZERO;

		BigDecimal valueHumanUnit = BigDecimal.ZERO;
		BigDecimal valueElfUnit = BigDecimal.ZERO;
		BigDecimal valueUndeadUnit = BigDecimal.ZERO;
		BigDecimal valueOrcUnit = BigDecimal.ZERO;

		BigDecimal thresholdMedal = new BigDecimal(1000);
		thresholdMedal = thresholdMedal.add(getPetSkillValueById(pets, "418", true));

		// only if Boom Boom is 5*
		PetWrap boomboom = getPetWrap(pets, "28");
		if (boomboom != null) {
			if (boomboom.getLevel().equals("5")) {
				Integer drummerCount = getUnitCount(unitsTimeShop, "102");
				thresholdMedal = thresholdMedal.add(new BigDecimal(drummerCount * 10));

				Integer drummerSeniorCount = getUnitCount(unitsTimeShop, "103");
				thresholdMedal = thresholdMedal.add(new BigDecimal(drummerSeniorCount * 20));
			}
		}

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
					if (pet.getTribe().equals(Tribe.HUMAN)) {
						BigDecimal petValue = getPetSkillValueById(pets, "414", true).divide(new BigDecimal(100));
						bonus = bonus.add(bonus.multiply(petValue));
						valueHumanUnit = valueHumanUnit.add(bonus);

					} else if (pet.getTribe().equals(Tribe.ELF)) {
						BigDecimal petValue = getPetSkillValueById(pets, "415", true).divide(new BigDecimal(100));
						bonus = bonus.add(bonus.multiply(petValue));
						valueElfUnit = valueElfUnit.add(bonus);

					} else if (pet.getTribe().equals(Tribe.UNDEAD)) {
						BigDecimal petValue = getPetSkillValueById(pets, "416", true).divide(new BigDecimal(100));
						bonus = bonus.add(bonus.multiply(petValue));
						valueUndeadUnit = valueUndeadUnit.add(bonus);

					} else if (pet.getTribe().equals(Tribe.ORC)) {
						BigDecimal petValue = getPetSkillValueById(pets, "417", true).divide(new BigDecimal(100));
						bonus = bonus.add(bonus.multiply(petValue));
						valueOrcUnit = valueOrcUnit.add(bonus);

					}
				}
				 
				valueUnit = valueUnit.add(bonus);
			}
		}

		for (UnitWrap unitWrap : unitsTeamRevive) {
			Unit unit = unitManager.getUnitByCode(unitWrap.getCode());
			if (unit != null) {
				Double valueT1 = 0.0;
				Double valueT2 = 0.0;
				Double valueT3 = 0.0;
				if (unit.getRank().equals("2")) {
					valueT1 = 0.0;
					valueT2 = 1.0;
					valueT3 = 1.0;
				} else if (unit.getRank().equals("3")) {
					valueT1 = 1.0;
					valueT2 = 2.0;
					valueT3 = 3.0;
				} else if (unit.getRank().equals("4")) {
					valueT1 = 3.0;
					valueT2 = 5.0;
					valueT3 = 10.0;
				} else if (unit.getRank().equals("5")) {
					valueT1 = 5.0;
					valueT2 = 10.0;
					valueT3 = 15.0;
				} else if (unit.getRank().equals("6")) {
					valueT1 = 10.0;
					valueT2 = 20.0;
					valueT3 = 30.0;
				}
				
				BigDecimal t1 = new BigDecimal(unitWrap.getCountT1()).multiply(new BigDecimal(valueT1));
				BigDecimal t2 = new BigDecimal(unitWrap.getCountT2()).multiply(new BigDecimal(valueT2));
				BigDecimal t3 = new BigDecimal(unitWrap.getCountT3()).multiply(new BigDecimal(valueT3));

				BigDecimal bonus = t1.add(t2).add(t3);

				Pet pet = petManager.getPetByUnitId(unitWrap.getCode());
				PetWrap petWrap = null;
				if (pet != null) {
					// pet 5* bonus +50%
					petWrap = getPetWrap(pets, pet.getKindNum());
					if (petWrap != null) {
						if (petWrap.getLevel().equals("5")) {
							bonus = bonus.multiply(new BigDecimal(1.5));
						}
					}
					// guardian bonus depends on level
					if (pet.getTribe().equals("1")) {
						BigDecimal petValue = getPetSkillValueById(pets, "414", true).divide(new BigDecimal(100));
						bonus = bonus.add(bonus.multiply(petValue));

					} else if (pet.getTribe().equals("2")) {
						BigDecimal petValue = getPetSkillValueById(pets, "415", true).divide(new BigDecimal(100));
						bonus = bonus.add(bonus.multiply(petValue));

					} else if (pet.getTribe().equals("3")) {
						BigDecimal petValue = getPetSkillValueById(pets, "416", true).divide(new BigDecimal(100));
						bonus = bonus.add(bonus.multiply(petValue));

					} else if (pet.getTribe().equals("4")) {
						BigDecimal petValue = getPetSkillValueById(pets, "417", true).divide(new BigDecimal(100));
						bonus = bonus.add(bonus.multiply(petValue));

					}
				}
				
				BigDecimal starBuff = new BigDecimal(unit.getStarBuff());
				if(unitWrap.getCode().equals("103")){
					if(petWrap != null && petWrap.getLevel().equals("5")){
						starBuff = new BigDecimal(40);
					}
				}
				
				starBuff = new BigDecimal(unitWrap.getCountT0()+unitWrap.getCountT1()+unitWrap.getCountT2()+unitWrap.getCountT3()).multiply(starBuff);
				
				valueUnitRevive = valueUnitRevive.add(bonus.add(starBuff));
			}
		}
		
		Integer knightCount = getUnitCount(unitsTimeShop, "174");
		Integer knightSrCount = getUnitCount(unitsTimeShop, "175");
		Integer wingCount = getUnitCount(unitsTimeShop, "176");
		Integer wingSrCount = getUnitCount(unitsTimeShop, "177");
		Integer abimeCount = getUnitCount(unitsTimeShop, "178");
		Integer abimeSrCount = getUnitCount(unitsTimeShop, "179");
		Integer sirenCount = getUnitCount(unitsTimeShop, "180");
		Integer sirenSrCount = getUnitCount(unitsTimeShop, "181");
		BigDecimal petHumanValue = getPetSkillValueById(pets, "511", true).divide(new BigDecimal(100));
		BigDecimal petElfValue = getPetSkillValueById(pets, "512", true).divide(new BigDecimal(100));
		BigDecimal petUndeadValue = getPetSkillValueById(pets, "513", true).divide(new BigDecimal(100));
		BigDecimal petOrcValue = getPetSkillValueById(pets, "514", true).divide(new BigDecimal(100));
		
		BigDecimal convivialityHuman = new BigDecimal(knightCount * 10).add(new BigDecimal(knightSrCount * 20));
		if(convivialityHuman.compareTo(new BigDecimal(400))==1){
			convivialityHuman = new BigDecimal(400);
		}
		convivialityHuman = convivialityHuman.add(convivialityHuman.multiply(petHumanValue));
		
		BigDecimal convivialityElf = new BigDecimal(wingCount * 10).add(new BigDecimal(wingSrCount * 20));
		if(convivialityElf.compareTo(new BigDecimal(400))==1){
			convivialityElf = new BigDecimal(400);
		}
		convivialityElf = convivialityElf.add(convivialityElf.multiply(petElfValue));
		
		BigDecimal convivialityUndead = new BigDecimal(abimeCount * 10).add(new BigDecimal(abimeSrCount * 20));
		if(convivialityUndead.compareTo(new BigDecimal(400))==1){
			convivialityUndead = new BigDecimal(400);
		}
		convivialityUndead = convivialityUndead.add(convivialityUndead.multiply(petUndeadValue));
		
		BigDecimal convivialityOrc = new BigDecimal(sirenCount * 10).add(new BigDecimal(sirenSrCount * 20));
		if(convivialityOrc.compareTo(new BigDecimal(400))==1){
			convivialityOrc = new BigDecimal(400);
		}
		convivialityOrc = convivialityOrc.add(convivialityOrc.multiply(petOrcValue));

		String valueUnitText = "";
		if (thresholdMedal.compareTo(valueUnit) < 0) {
			total = total.add(thresholdMedal);
			valueUnitText = thresholdMedal + " % ("+ valueUnit +")"; 
		} else {
			total = total.add(valueUnit);
			valueUnitText = valueUnit + " %"; 
		}

		total = total.add(valueArte).add(valuePet).add(valueUnitRevive);

		//@formatter:off
		String result = "<th style='color: #ddaa2a;font-weight: bold' colspan='6'>" + label + "  </th>" + 
		"<tr>" + 
		"	<td style='color: #ddaa2a;font-weight: bold'>Unité en magasin : </td>" + 
		"	<td style='color: #dcdcdc;'>" + valueUnitText + "</td>" + 
		"</tr>" + 
		"<tr style='color: #ddaa2a;font-weight: bold'>" + 
		"	<td>Humain</td>" + 
		"	<td>Elfe</td>" + 
		"	<td>Orc</td>" + 
		"	<td>Mort Vivant</td>" + 
		"</tr>" + 
		"<tr style='color: #dcdcdc;'> " + 
		"	<td>" + valueHumanUnit + " %</td>" + 
		"	<td>" + valueElfUnit + " %</td>" + 
		"	<td>" + valueOrcUnit + " %</td>" + 
		"	<td>" + valueUndeadUnit + " %</td>" + 
		"</tr>" + 
		"<th style='color: #ddaa2a;font-weight: bold' colspan='6'>Seuil maximum dans le magasin du temps : "+thresholdMedal+" %</th>" + 
		"<tr>" + 
		"	<td style='color: #ddaa2a;font-weight: bold'>Unité dans la <br/>team spécialisée</td>" + 
		"	<td style='color: #dcdcdc;'>" + valueUnitRevive + " %</td>" + 
		"</tr>" + 
		"<tr>" + 
		"	<td style='color: #ddaa2a;font-weight: bold'>Unité dans la <br/>team courante</td>" + 
		"	<td style='color: #dcdcdc;'>Non calculé</td>" + 
		"</tr>" + 
		"<tr>" + 
		"	<td style='color: #ddaa2a;font-weight: bold'>Artéfact</td>" + 
		"	<td style='color: #dcdcdc;'>" + valueArte + " %</td>" + 
		"</tr>" + 
		"<tr>" + 
		"	<td style='color: #ddaa2a;font-weight: bold'>Pet</td>" + 
		"	<td style='color: #dcdcdc;'>" + valuePet + " %</td>" + 
		"</tr>" + 
		"<tr>" + 
		"	<td style='color: #ddaa2a;font-weight: bold'>Magasin</td>" + 
		"	<td style='color: #dcdcdc;'>300 %</td>" + 
		"</tr>" + 
		"<tr>" + 
		"	<td style='color: #ddaa2a;font-weight: bold'>Niveau KL</td>" + 
		"	<td style='color: #dcdcdc;'>200 %</td>" + 
		"</tr>" + 
		"<tr>" + 
		"	<td style='color: #ddaa2a;font-weight: bold'>Guilde</td>" + 
		"	<td style='color: #dcdcdc;'>350 %</td>" + 
		"</tr>" + 
		"<tr style='color: #ddaa2a;font-weight: bold'>" + 
		"	<td>Convivialité</td>" + 
		"</tr>" + 
		"<tr style='color: #ddaa2a;font-weight: bold'> " + 
		"	<td>Humain</td>" + 
		"	<td>Elfe</td>" + 
		"	<td>Orc</td>" + 
		"	<td>Mort Vivant</td>" + 
		"</tr>" + 
		"<tr style='color: #dcdcdc;'> " + 
		"	<td>" + convivialityHuman + " %</td>" + 
		"	<td>" + convivialityElf + " %</td>" + 
		"	<td>" + convivialityUndead + " %</td>" + 
		"	<td>" + convivialityOrc + " %</td>" + 
		"</tr>" + 
		"<tr style='color: #ddaa2a;font-weight: bold'>" + 
		"	<td>Total</td>" + 
		"</tr>" + 
		"<tr style='color: #ddaa2a;font-weight: bold'> " + 
		"	<td>Humain</td>" + 
		"	<td>Elfe</td>" + 
		"	<td>Orc</td>" + 
		"	<td>Mort Vivant</td>" + 
		"</tr>" + 
		"<tr style='color: #dcdcdc;'> " + 
		"	<td>" + total.add(convivialityHuman).add(new BigDecimal(300)).add(new BigDecimal(200)).add(new BigDecimal(350))+ " %</td>" + 
		"	<td>" + total.add(convivialityElf).add(new BigDecimal(300)).add(new BigDecimal(200)).add(new BigDecimal(350))+ " %</td>" + 
		"	<td>" + total.add(convivialityUndead).add(new BigDecimal(300)).add(new BigDecimal(200)).add(new BigDecimal(350))+ " %</td>" + 
		"	<td>" + total.add(convivialityOrc).add(new BigDecimal(300)).add(new BigDecimal(200)).add(new BigDecimal(350))+ " %</td>" + 
		"</tr>";
		//@formatter:on

		return result;
	}

	private Integer getUnitCount(List<UnitWrap> unitsTimeShop, String code) {

		for (UnitWrap unitWrap : unitsTimeShop) {
			if (unitWrap.getCode().equals(code)) {
				return unitWrap.getCountT0() + unitWrap.getCountT1() + unitWrap.getCountT2() + unitWrap.getCountT3();
			}
		}
		return 0;
	}

	private PetWrap getPetWrap(List<PetWrap> pets, String kindNum) {

		for (PetWrap petWrap : pets) {
			if (petWrap.getId().equals(kindNum)) {
				return petWrap;
			}
		}
		return null;
	}

	private BigDecimal getSkillValueByCode(List<ArtefactWrap> arte, String arteSkillCode) {
		BigDecimal result = BigDecimal.ZERO;
		if(arteSkillCode==null){
			return result;
		}
		
		for (ArtefactWrap artefact : arte) {

			Treasure treasure = treasureManager.getTreasureById(artefact.getId());
			if (treasure.getSkillCode1().equals(arteSkillCode)) {
				if (artefact.getTrans().equals("0"))
					result = result.add(new BigDecimal(treasureManager.getAbilityValue(treasure.getAbility1(), artefact.getLevel())));

				if (artefact.getTrans().equals("1"))
					result = result.add(new BigDecimal(treasureManager.getAbilityValue(treasure.getAbility11(), artefact.getLevel())));

				if (artefact.getTrans().equals("2"))
					result = result.add(new BigDecimal(treasureManager.getAbilityValue(treasure.getAbility21(), artefact.getLevel())));

			}
			if (treasure.getSkillCode2().equals(arteSkillCode)) {
				if (artefact.getTrans().equals("0"))
					result = result.add(new BigDecimal(treasureManager.getAbilityValue(treasure.getAbility2(), artefact.getLevel())));

				if (artefact.getTrans().equals("1"))
					result = result.add(new BigDecimal(treasureManager.getAbilityValue(treasure.getAbility12(), artefact.getLevel())));

				if (artefact.getTrans().equals("2"))
					result = result.add(new BigDecimal(treasureManager.getAbilityValue(treasure.getAbility22(), artefact.getLevel())));

			}
			if (treasure.getSkillCode3().equals(arteSkillCode)) {
				// Not implement in EF side
			}
		}
		return result;
	}

	private BigDecimal getSkillSetValueByCode(List<ArtefactSetWrap> arteSet, String skillArteCode) {
		BigDecimal result = BigDecimal.ZERO;
		for (ArtefactSetWrap setWrap : arteSet) {
			TreasureSet set = treasureManager.getTreasureSetById(setWrap.getId());

			String trans = setWrap.getTrans();
			List<String> skilllist = Arrays.asList(set.getSkillList().split("\\|"));

			// Calcul of index for bonus of artefact set depends, 2 piece, 3
			// piece, 4 piece or full set
			Integer index = 0;
			String numSetList = set.getNumSetList();
			for (String numSet : numSetList.split("\\|")) {
				if (setWrap.getLevel() >= Integer.parseInt(numSet)) {
					index++;
				}
			}

			if (trans.equals("0")) {
				List<String> valuelist = Arrays.asList(set.getValueList().split("\\|"));

				for (int i = 0; i < index; i++) {
					String[] skillSets = skilllist.get(i).split("#");
					String[] valueSets = valuelist.get(i).split("#");

					for (int j = 0; j < skillSets.length; j++) {
						if (skillSets[j].equals(skillArteCode)) {
							result = result.add(new BigDecimal(valueSets[j]));
						}
					}
				}
			} else if (trans.equals("1")) {
				List<String> valuelist = Arrays.asList(set.getValueList1().split("\\|"));
				for (int i = 0; i < index; i++) {
					String[] skillSets = skilllist.get(i).split("#");
					String[] valueSets = valuelist.get(i).split("#");

					for (int j = 0; j < skillSets.length; j++) {
						Skill skill = new Skill();
						skill.setCode(skillSets[j]);

						if (skillSets[j].equals(skillArteCode)) {
							result = result.add(new BigDecimal(valueSets[j]));
						}
					}
				}
			} else if (trans.equals("2")) {
				List<String> valuelist = Arrays.asList(set.getValueList2().split("\\|"));
				for (int i = 0; i < index; i++) {
					String[] skillSets = skilllist.get(i).split("#");
					String[] valueSets = valuelist.get(i).split("#");

					for (int j = 0; j < skillSets.length; j++) {
						Skill skill = new Skill();
						skill.setCode(skillSets[j]);

						if (skillSets[j].equals(skillArteCode)) {
							result = result.add(new BigDecimal(valueSets[j]));
						}
					}
				}
			}
		}
		return result;
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

	public QuestList getQuestList() {
		return questList;
	}

	public void setQuestList(QuestList questList) {
		this.questList = questList;
	}

}
