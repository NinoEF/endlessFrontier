package net.phenix.discord.bot.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

public class SheetManager {

	Logger log = Logger.getLogger(getClass());

	/** Application name. */
	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	private Map<String, String> progressions = new HashMap<String, String>();

	private Sheets service = null;

	public static String spreadsheetId = "1-AJ8kCAbq92Bqx3e6o1dBIE6AGlxZW_YRtesmJTk6JU";
	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/sheets.googleapis.com-java-quickstart
	 */
	private final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

	public static SheetManager getInstance() {
		return new SheetManager();
	}

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = SheetManager.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline")
				.build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		log.info("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Sheets API client service.
	 * 
	 * @return an authorized Sheets API client service
	 * @throws IOException
	 */
	public Sheets getSheetsService() throws IOException {
		Credential credential = authorize();
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	public void init() throws IOException {
		setService(getSheetsService());

		log.info("SheetManager : Init done");
	}

	public String getUserProgression(String user) throws IOException {
		return progressions.get(user);
	}

	public Sheets getService() {
		return service;
	}

	public void setService(Sheets service) {
		this.service = service;
	}

	public List<List<Object>> getValues(Sheet sheet, int colStart, int rowStart, int colEnd, int rowEnd) {
		List<List<Object>> result = new ArrayList();
		for (int i = rowStart; i <= rowEnd; i++) {

			Row currentRow = sheet.getRow(i);
			if (currentRow != null) {
				List<Object> row = new ArrayList();
				for (int j = colStart; j <= colEnd; j++) {
					Cell cell = currentRow.getCell(j);
					if (cell != null) {
						if (cell.getCellTypeEnum() == CellType.STRING) {
							row.add(cell.getStringCellValue());
						} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
							Double d = cell.getNumericCellValue();
							row.add(String.valueOf(d.intValue()));
						} else if (cell.getCellTypeEnum() == CellType.FORMULA) {
							try {
								Double d = cell.getNumericCellValue();
								row.add(String.valueOf(d.intValue()));
							} catch (Exception e) {
								try {
									row.add(cell.getStringCellValue());
								} catch (Exception e1) {
									row.add("");
								}
							}
						}
					} else {
						row.add("");
					}
				}
				result.add(row);
			}
		}
		return result;
	}
}