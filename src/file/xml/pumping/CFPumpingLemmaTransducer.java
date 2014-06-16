package file.xml.pumping;

import java.util.ArrayList;
import java.util.List;

import model.pumping.Case;
import model.pumping.ContextFreePumpingLemma;
import model.pumping.PumpingLemma;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import file.xml.XMLHelper;

/**
 * Transducer for saving any Context-Free Pumping Lemma.
 * 
 * @author Jinghui Lim, Ian McMahon
 *
 */
public class CFPumpingLemmaTransducer extends PumpingLemmaTransducer{
	
	public static final String CF_LEMMA_TAG = "context-free pumping lemma";
    private static final String U_NAME = "uLength";
    private static final String V_NAME = "vLength";
    private static final String CASE_NAME = "case";
    private static final String CASE_I_NAME = "caseI";
    private static final String CASE_U_NAME = "caseULength";
    private static final String CASE_V_NAME = "caseVLength";
    private static final String CASE_X_NAME = "caseXLength";
    private static final String CASE_Y_NAME = "caseYLength";

	@Override
	public String getTag() {
		return CF_LEMMA_TAG;
	}

	@Override
	public PumpingLemma fromStructureRoot(Element root) {
		 ContextFreePumpingLemma pl = (ContextFreePumpingLemma)PumpingLemmaFactory.createPumpingLemma
		            (CF_LEMMA_TAG, root.getElementsByTagName(LEMMA_NAME).item(0).getTextContent());
		        setMWI(root, pl);       

		        /*
		         * Decode cases. 
		         * 
		         * Must decode cases before decoding the decomposition, otherwise 
		         * the decomposition will be that of the last case. This is because,
		         * when add case is called, the pumping lemma chooses the decomposition
		         * to check if it's legal. 
		         */
		        readCases(root, pl);

		        //Decode the attempts
		        NodeList attempts = root.getElementsByTagName(ATTEMPT);
		        for(int i = 0; i < attempts.getLength(); i++)
		            pl.addAttempt(attempts.item(i).getTextContent());        
		        
		        //Decode the first player.         
		        pl.setFirstPlayer(root.getElementsByTagName(FIRST_PLAYER).item(0).getTextContent());
		             
		        // Decode the decomposition.
		        int uLength = Integer.parseInt(root.getElementsByTagName(U_NAME).item(0).getTextContent());
		        int vLength = Integer.parseInt(root.getElementsByTagName(V_NAME).item(0).getTextContent());
		        int xLength = Integer.parseInt(root.getElementsByTagName(X_NAME).item(0).getTextContent());
		        int yLength = Integer.parseInt(root.getElementsByTagName(Y_NAME).item(0).getTextContent());
		        
		        if(!pl.setDecomposition(new int[]{uLength, vLength, xLength, yLength})){
		        	pl.reset();
		        	setMWI(root, pl);
		        }
		        
		        //Return!
		        return pl;
	}

	/**
	 * Decode m, w, & i.
	 */
	private void setMWI(Element root, ContextFreePumpingLemma pl) {
		pl.setM(Integer.parseInt(root.getElementsByTagName(M_NAME).item(0).getTextContent()));
		pl.setW(root.getElementsByTagName(W_NAME).item(0).getTextContent());
		pl.setI(Integer.parseInt(root.getElementsByTagName(I_NAME).item(0).getTextContent()));
	}
	
	
	
    private void readCases(Element root, ContextFreePumpingLemma pl)
    {
        NodeList caseNodes = root.getElementsByTagName(CASE_NAME);
        for(int i = 0; i < caseNodes.getLength(); i++)
        {
            Node caseNode = caseNodes.item(i);
            if(caseNode.getNodeType() != Node.ELEMENT_NODE)
                continue;
            int u = Integer.parseInt(((Element)caseNode).getElementsByTagName(CASE_U_NAME).item(0).getTextContent());
            int v = Integer.parseInt(((Element)caseNode).getElementsByTagName(CASE_V_NAME).item(0).getTextContent());
            int x = Integer.parseInt(((Element)caseNode).getElementsByTagName(CASE_X_NAME).item(0).getTextContent());
            int y = Integer.parseInt(((Element)caseNode).getElementsByTagName(CASE_Y_NAME).item(0).getTextContent());
            int j = Integer.parseInt(((Element)caseNode).getElementsByTagName(CASE_I_NAME).item(0).getTextContent());
            pl.addCase(new int[]{u, v, x, y}, j);
        }
    }

	@Override
	public Element appendComponentsToRoot(Document doc, PumpingLemma structure,
			Element root) {
		
		ContextFreePumpingLemma pl = (ContextFreePumpingLemma)structure;
		
        root.appendChild(XMLHelper.createElement(doc, LEMMA_NAME,  pl.getName(), null));
        root.appendChild(XMLHelper.createElement(doc, FIRST_PLAYER,  pl.getFirstPlayer(), null));
        root.appendChild(XMLHelper.createElement(doc, M_NAME,  "" + pl.getM(), null));
        root.appendChild(XMLHelper.createElement(doc, W_NAME,  "" + pl.getW(), null));
        root.appendChild(XMLHelper.createElement(doc, I_NAME,  "" + pl.getI(), null));
        root.appendChild(XMLHelper.createElement(doc, U_NAME,  "" + pl.getU().length(), null));
        root.appendChild(XMLHelper.createElement(doc, V_NAME,  "" + pl.getV().length(), null));
        root.appendChild(XMLHelper.createElement(doc, X_NAME,  "" + pl.getX().length(), null));
        root.appendChild(XMLHelper.createElement(doc, Y_NAME,  "" + pl.getY().length(), null));
        
        //Encode the list of attempts.
        List<String> attempts = pl.getAttempts();
        if(attempts != null && attempts.size() > 0)        
            for(int i = 0; i < attempts.size(); i++)
                root.appendChild(XMLHelper.createElement(doc, ATTEMPT, (String)attempts.get(i), null));
                
        //Encode the list of done cases.
        ArrayList<Case> cases = pl.getDoneCases();
        if(cases != null && cases.size() > 0)
            for(int i = 0; i < cases.size(); i++)
                root.appendChild(createCaseElement(doc, (Case)cases.get(i)));
        
        return root;
	}

	private Element createCaseElement(Document doc, Case c)
    {
        Element elem = XMLHelper.createElement(doc, CASE_NAME, null, null);
        
        int[] decomposition = c.getInput();
        elem.appendChild(XMLHelper.createElement(doc, CASE_U_NAME, "" + decomposition[0], null));
        elem.appendChild(XMLHelper.createElement(doc, CASE_V_NAME, "" + decomposition[1], null));
        elem.appendChild(XMLHelper.createElement(doc, CASE_X_NAME, "" + decomposition[2], null));
        elem.appendChild(XMLHelper.createElement(doc, CASE_Y_NAME, "" + decomposition[3], null));
        elem.appendChild(XMLHelper.createElement(doc, CASE_I_NAME, "" + c.getI(), null));
        return elem;
    }
}
