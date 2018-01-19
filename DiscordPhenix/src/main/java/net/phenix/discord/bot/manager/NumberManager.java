package net.phenix.discord.bot.manager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public class NumberManager {

	Logger log = Logger.getLogger(getClass());
	
	static String[] letters = {"", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z","aa","ab","ac","ad","ae","af","ag","ah","ai","aj","ak","al","am","an","ao","ap","aq","ar","as","at","au","av","aw","ax","ay","az","ba","bb","bc","bd","be","bf","bg","bh","bi","bj","bk","bl","bm","bn","bo","bp","bq","br","bs","bt","bu","bv","bw","bx","by","bz","ca","cb","cc","cd","ce","cf","cg","ch","ci","cj","ck","cl","cm","cn","co","cp","cq","cr","cs","ct","cu","cv","cw","cx","cy","cz","da","db","dc","dd","de","df","dg","dh","di","dj","dk","dl","dm","dn","do","dp","dq","dr","ds","dt","du","dv","dw","dx","dy","dz","ea","eb","ec","ed","ee","ef","eg","eh","ei","ej","ek","el","em","en","eo","ep","eq","er","es","et","eu","ev","ew","ex","ey","ez","fa","fb","fc","fd","fe","ff","fg","fh","fi","fj","fk","fl","fm","fn","fo","fp","fq","fr"};

	static List<String> lettersList = Arrays.asList(letters);

	static int count = 0;

	private static BigDecimal getNumberPart(BigDecimal number) {
		if (number.compareTo(new BigDecimal(1000)) > 0) {
			number = getNumberPart(number.divide(new BigDecimal(1000)));
			count++;
		}
		return number;
	}
	
	public static String getEFNumber(BigDecimal number){
		count = 0;
		DecimalFormat df = new DecimalFormat("0.##");
		return df.format(getNumberPart(number).doubleValue()) + lettersList.get(count);
	}
	
	public static BigDecimal getNumber(String value){
		String letter = value.replaceAll("[^a-z]", ""); 
		String number = value.replaceAll(",", ".").replaceAll("[^0-9\\.]", "");
		
		int exposant = lettersList.indexOf(letter);
		
		BigDecimal power = new BigDecimal(1);
		for (int i = 0; i < exposant; i++) {
			power = power.multiply(new BigDecimal(1000));
		}
		power = power.multiply(new BigDecimal(number));
		
		return power;
		
	}
}
