package net.phenix.discord.bot.listener;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.google.common.io.Files;

import gui.ava.html.image.generator.HtmlImageGenerator;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.phenix.discord.bot.data.PetWrap;
import net.phenix.discord.bot.data.Skill;
import net.phenix.discord.bot.data.UnitWrap;
import net.phenix.discord.bot.manager.BattleManager;
import net.phenix.discord.bot.manager.BuildManager;
import net.phenix.discord.bot.manager.BundleManager;
import net.phenix.discord.bot.manager.NumberManager;
import net.phenix.discord.bot.manager.PetManager;
import net.phenix.discord.bot.manager.SheetManager;
import net.phenix.discord.bot.manager.TreasureManager;
import net.phenix.discord.bot.manager.UnitManager;

public class CmdListener extends ListenerAdapter {

	Logger log = Logger.getLogger(getClass());

	private UnitManager unitManager;

	private SheetManager sheetManager;

	private BattleManager battleManager;

	private PetManager petManager;

	private TreasureManager treasureManager;

	private BundleManager bundleManager;

	private BuildManager buildManager;

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;

		MessageChannel channel = event.getChannel();

		// We don't want to respond to other bot accounts, including ourself
		Message message = event.getMessage();
		String content = message.getContentRaw();
		if (content.startsWith("!")) {
			log.info(event.getAuthor().getName() + " : " + content);
			// getRawContent() is an atomic getter
			// getContent() is a lazy getter which modifies the content for e.g.
			// console view (strip discord formatting)
			if (content.equals("!help")) {
				channel.sendMessage("!ping - ...\n" + "!petfc <nb fragment> <lvl pet> <nb fragment/jour> - Vous donne le planning d'obtention.\n" + "\n"
						+ "!revive <etage> <bonus> - Vous donne le nombre de médaille pour un étage donné et un bonus donné\n"
						+ "!revive <etage> <bonus> <temps> - Vous donne le repos pour un étage donné, un bonus donné et le temps de run\n" + "\n"
						+ "!unit list <Nombre d'étoile> - Vous donne la liste des unités avec leur identifiant selon le nombre d'étoile\n"
						+ "!unit list <race> - Vous donne la liste des unités avec leur identifiant selon la race (human, elf, orc, undead)\n" + "\n"
						+ "!unit goldlevel <goldLevel> <id de l'unite> <bonus de réduction> - Vous donne le total d'or pour maxer une unité\n" + "\t exemple : !unit goldlevel 3122 159 423d\n"
						+ "\t résultat : 2,48at\n").queue();

			} else if (content.equals("!ping")) {
				// Important to call .queue() on the RestAction returned by
				// sendMessage(...)
				channel.sendMessage("Pong!").queue();

			} else if (content.startsWith("!petfc")) {
				petManager.petForecast(channel, content);

			} else if (content.startsWith("!revive")) {
				try {
					String[] args = content.split(" ");
					Integer floor = Integer.parseInt(args[1]);
					if (floor > 30000) {
						channel.sendMessage("L'étage maximun est 30000").queue();
						return;
					}

					BigDecimal bonus = new BigDecimal(Integer.parseInt(args[2]));

					String response = "";

					if (args.length == 4) {
						Integer time = Integer.parseInt(args[3]);
						BigDecimal total = battleManager.getRevivalSpiritRest(floor, bonus, time);

						response = NumberManager.getEFNumber(total) + "/min";
					} else {
						BigDecimal total = battleManager.getRevivalMedalsQuantity(floor, bonus);

						response = NumberManager.getEFNumber(total);
					}

					channel.sendMessage(response).queue();

				} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (content.startsWith("!excel")) {
				String[] args = content.split(" ");

				try {
					channel.sendMessage(sheetManager.getUserProgression(args[1])).queue();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (content.startsWith("!unit")) {
				String[] args = content.split(" ");

				try {
					if (args[1].toLowerCase().equals("list")) {

						if (args[2].matches("[0-9]+")) {
							channel.sendMessage(unitManager.getUnitsListByRank(args[2])).queue();
						} else {
							channel.sendMessage(unitManager.getUnitsListByTribe(args[2])).queue();
						}
					} else if (args[1].toLowerCase().equals("goldlevel")) {

						if (args.length != 5) {
							channel.sendMessage("Nombre de paramétre incorrect").queue();
							return;
						}
						Integer goldLevel = Integer.parseInt(args[2]);
						if (goldLevel > 3400) {
							channel.sendMessage("Le gold level maximun est de 3400").queue();
							;
							return;
						}

						Integer unitID = Integer.parseInt(args[3]);
						String bonusReduction = args[4];

						channel.sendMessage(unitManager.getTotalUpgradeGoldLevel(goldLevel, unitID, bonusReduction)).queue();
					}
				} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Guild guild = event.getGuild();
				//
				// String[] args = content.split(" ");
				// String id = args[1].substring(2, args[1].length() - 1);
				// Member member = guild.getMemberById(id);
				// User user = member.getUser();
				// if (user != null) {
				// user.openPrivateChannel().queue((pvChannel) -> {
				// pvChannel.sendMessage(embed).queue();
				// });
				// }
			} else if (content.startsWith("!stat")) {
				try {
					String[] args = content.split(" ");
					String spreadsheetId = "";
					if (args.length != 1) {
						if (args[1].equals("init")) {
							if (args.length == 2) {
								channel.sendMessage("Commande incorrect, il manque l'ID de l'excel : !stat init <excelId>").queue();
							} else if (args.length != 3) {
								channel.sendMessage("Commande incorrect, il y a trop d'argument: !stat init <excelId>").queue();
							}
							String excelId = content.substring("!stat init ".length());
							File directory = new File(String.valueOf("/home/pi/discord/user/" + event.getAuthor().getId()));
							if (!directory.exists()) {
								directory.mkdir();
							}
							File file = new File(directory.getPath() + "/excel_id.txt");
							if(file.exists()){
								channel.sendMessage("Mis à jour du lien du Excel.").queue();
							} else {
								channel.sendMessage("Création du lien Excel.").queue();
							}
							Files.write(excelId, file, Charset.forName("UTF-8"));
							return;
						} else {
							channel.sendMessage("Commande incorrect, !stat init <excelId>").queue();
						}
					} else {

						File file = new File("/home/pi/discord/user/" + event.getAuthor().getId()+ "/excel_id.txt");
						if(!file.exists()){
							channel.sendMessage("Veuillez créer votre lien avec votre excel en tapant !stat init <excelId>").queue();
							return;
						}
						try (BufferedReader br = new BufferedReader(new FileReader(file))) {
							spreadsheetId = br.readLine();
						} catch (IOException e) {
						}
					}
					

					List<Skill> skillArte = new ArrayList<>();
					List<Skill> skillPet = new ArrayList<>();
					List<UnitWrap> unitsTimeShop = new ArrayList<>();
					List<UnitWrap> unitsTeamRevive = new ArrayList<>();
					
					List<PetWrap> pets = new ArrayList<>();
					
					buildManager.build(skillArte, skillPet, pets, unitsTimeShop,unitsTeamRevive, sheetManager, treasureManager, petManager, spreadsheetId);

					String result = buildManager.compute(skillArte, skillPet, pets,  unitsTimeShop, unitsTeamRevive);

					HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
					imageGenerator.loadHtml(result);

					BufferedImage img = imageGenerator.getBufferedImage();

					ByteArrayOutputStream os = new ByteArrayOutputStream();
					try {
						ImageIO.write(img, "png", os);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					InputStream is = new ByteArrayInputStream(os.toByteArray());

					channel.sendFile(is, "test.png").queue();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (content.startsWith("!annonce1")) {

				String text = content.substring(9);

				/*
				 * Because font metrics is based on a graphics context, we need
				 * to create a small, temporary image so we can ascertain the
				 * width and height of the final image
				 */
				BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = img.createGraphics();
				Font font = new Font("Arial", Font.PLAIN, 48);
				g2d.setFont(font);
				FontMetrics fm = g2d.getFontMetrics();
				int width = fm.stringWidth(text);
				int height = fm.getHeight();
				g2d.dispose();

				img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				g2d = img.createGraphics();
				g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
				g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
				g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				g2d.setFont(font);
				fm = g2d.getFontMetrics();
				g2d.setColor(Color.BLACK);
				g2d.drawString(text, 0, fm.getAscent());
				g2d.dispose();

				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					ImageIO.write(img, "png", os);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				InputStream is = new ByteArrayInputStream(os.toByteArray());

				channel.sendFile(is, "test.png").queue();
			} else if (content.startsWith("!annonce2")) {

				String text = content.substring(9);

				HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
				imageGenerator.loadHtml(text);

				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					ImageIO.write(imageGenerator.getBufferedImage(), "png", os);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				InputStream is = new ByteArrayInputStream(os.toByteArray());

				channel.sendFile(is, "test.png").queue();
			}
		}
	}

	public UnitManager getUnitManager() {
		return unitManager;
	}

	public void setUnitManager(UnitManager unitManager) {
		this.unitManager = unitManager;
	}

	public SheetManager getSheetManager() {
		return sheetManager;
	}

	public void setSheetManager(SheetManager sheetManager) {
		this.sheetManager = sheetManager;
	}

	public BattleManager getBattleManager() {
		return battleManager;
	}

	public void setBattleManager(BattleManager battleManager) {
		this.battleManager = battleManager;
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

	public BundleManager getBundleManager() {
		return bundleManager;
	}

	public void setBundleManager(BundleManager bundleManager) {
		this.bundleManager = bundleManager;
	}

	public BuildManager getBuildManager() {
		return buildManager;
	}

	public void setBuildManager(BuildManager buildManager) {
		this.buildManager = buildManager;
	}

}