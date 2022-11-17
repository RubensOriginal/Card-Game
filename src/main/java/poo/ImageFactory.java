package poo;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageFactory {
	private static ImageFactory imgf = new ImageFactory();
	private Map<String, Image> images;

	public static ImageFactory getInstance() {
		return imgf;
	}

	private ImageFactory() {
		images = new HashMap<>();
	}

	private String id2File(String imgId) {
		switch (imgId) {
		case "img1":
			return ("/imagens/Um.png");
		case "img2":
			return ("/imagens/Dois.png");
		case "img3":
			return ("/imagens/Tres.png");
		case "img4":
			return ("/imagens/Quatro.png");
		case "img5":
			return ("/imagens/Cinco.png");
		case "img6":
			return ("/imagens/Seis.png");
		case "img7":
			return ("/imagens/Sete.png");
		case "img8":
			return ("/imagens/Oito.png");
		case "img9":
			return ("/imagens/Nove.png");
		case "img10":
			return ("/imagens/Dez.png");
		case "imgBck":
			return ("/imagens/Back.png");
		default:
			throw new IllegalArgumentException("Invalid image Id");
		}
	}

	public ImageView createImage(String imgId) {
		Image img = images.get(imgId);
		if (img == null) {
			// img = new Image("/cards/4031928.jpg");
			img = new Image(getClass().getResourceAsStream(imgId));
			images.put(imgId, img);
		}

		ImageView imgv = new ImageView(img);
		// imgv.setFitHeight(307);
		// imgv.setFitWidth(210);
		imgv.setFitHeight(307/4);
		imgv.setFitWidth(210/4);
		return imgv;
	}
}
