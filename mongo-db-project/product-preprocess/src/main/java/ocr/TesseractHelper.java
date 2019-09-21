package ocr;

import java.awt.Image;
import java.io.File;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class TesseractHelper {
	
	private static Tesseract TESSERACT = null;
	
	private static Tesseract getTesseract() {
		Tesseract instance = new Tesseract();
		instance.setDatapath("/usr/local/Cellar/tesseract/4.1.0/share/tessdata");
		instance.setLanguage("eng");
		instance.setHocr(true);
		return instance;
	}

	public static String doOCR(File file) {
		// fill Singleton
		if (TESSERACT == null) {
			TESSERACT = getTesseract();
		}
		try {
			return TESSERACT.doOCR(file);
		} catch (TesseractException e) {
			e.printStackTrace();
			return null;
		}
	}
}
