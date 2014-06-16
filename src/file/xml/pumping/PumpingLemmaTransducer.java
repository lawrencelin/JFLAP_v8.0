package file.xml.pumping;

import model.pumping.PumpingLemma;
import file.xml.StructureTransducer;


public abstract class PumpingLemmaTransducer extends StructureTransducer<PumpingLemma>
{
    public static String LEMMA_NAME = "name";
    public static String FIRST_PLAYER = "first_player";
    public static String M_NAME = "m";
    public static String W_NAME = "w";
    public static String I_NAME = "i";
	public static final String X_NAME = "xLength";
	public static final String Y_NAME = "yLength";
    public static String ATTEMPT = "attempt";
    public static String COMMENT_M = "The user's input of m.";
    public static String COMMENT_I = "The program's value of i.";
}
