package file.xml.pumping;

import java.util.List;

import model.pumping.PumpingLemma;
import model.pumping.RegularPumpingLemma;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import file.xml.XMLHelper;

/**
 * Transducer for saving any Regular Pumping Lemma.
 * 
 * @author Jinghui Lim, Ian McMahon
 *
 */
public class RegPumpingLemmaTransducer extends PumpingLemmaTransducer {

	public static final String REG_LEMMA_TAG = "regular pumping lemma";

	@Override
	public String getTag() {
		return REG_LEMMA_TAG;
	}

	@Override
	public PumpingLemma fromStructureRoot(Element root) {
		RegularPumpingLemma pl = (RegularPumpingLemma) PumpingLemmaFactory
				.createPumpingLemma(REG_LEMMA_TAG,
						root.getElementsByTagName(LEMMA_NAME).item(0)
								.getTextContent());

		// Decode m, w, & i.
		pl.setM(Integer.parseInt(root.getElementsByTagName(M_NAME).item(0)
				.getTextContent()));
		pl.setW(root.getElementsByTagName(W_NAME).item(0).getTextContent());
		pl.setI(Integer.parseInt(root.getElementsByTagName(I_NAME).item(0)
				.getTextContent()));

		// Decode the attempts
		NodeList attempts = root.getElementsByTagName(
				ATTEMPT);
		for (int i = 0; i < attempts.getLength(); i++)
			pl.addAttempt(attempts.item(i).getTextContent());

		// Decode the first player.
		pl.setFirstPlayer(root.getElementsByTagName(FIRST_PLAYER).item(0)
				.getTextContent());

		// Decode the decomposition.
		int xLength = Integer.parseInt(root.getElementsByTagName(X_NAME)
				.item(0).getTextContent());
		int yLength = Integer.parseInt(root.getElementsByTagName(Y_NAME)
				.item(0).getTextContent());
		pl.setDecomposition(new int[] { xLength, yLength });

		return pl;
	}

	@Override
	public Element appendComponentsToRoot(Document doc, PumpingLemma structure,
			Element root) {
		RegularPumpingLemma pl = (RegularPumpingLemma) structure;

		root.appendChild(XMLHelper.createElement(doc, LEMMA_NAME, pl.getName(),
				null));
		root.appendChild(XMLHelper.createElement(doc, FIRST_PLAYER,
				pl.getFirstPlayer(), null));
		root.appendChild(XMLHelper.createElement(doc, M_NAME, "" + pl.getM(),
				null));
		root.appendChild(XMLHelper.createElement(doc, W_NAME, "" + pl.getW(),
				null));
		root.appendChild(XMLHelper.createElement(doc, I_NAME, "" + pl.getI(),
				null));
		root.appendChild(XMLHelper.createElement(doc, X_NAME, ""
				+ pl.getX().length(), null));
		root.appendChild(XMLHelper.createElement(doc, Y_NAME, ""
				+ pl.getY().length(), null));

		// Encode the list of attempts.
		List<String> attempts = pl.getAttempts();
		if (attempts != null && attempts.size() > 0)
			for (int i = 0; i < attempts.size(); i++)
				root.appendChild(XMLHelper.createElement(doc, ATTEMPT,
						attempts.get(i), null));

		return root;
	}

}
