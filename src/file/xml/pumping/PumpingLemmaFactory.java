package file.xml.pumping;

import model.pumping.PumpingLemma;
import model.pumping.cf.AiBjCk;
import model.pumping.cf.AkBnCnDj;
import model.pumping.cf.AnBjAnBj;
import model.pumping.cf.AnBnCn;
import model.pumping.cf.NaNbNc;
import model.pumping.cf.NagNbeNc;
import model.pumping.cf.W1BnW2;
import model.pumping.cf.W1CW2CW3CW4;
import model.pumping.cf.W1VVrW2;
import model.pumping.cf.WW;
import model.pumping.cf.WW1WrEquals;
import model.pumping.cf.WW1WrGrtrThanEq;
import model.pumping.reg.AB2n;
import model.pumping.reg.ABnAk;
import model.pumping.reg.AnBk;
import model.pumping.reg.AnBkCnk;
import model.pumping.reg.AnBlAk;
import model.pumping.reg.AnBn;
import model.pumping.reg.AnEven;
import model.pumping.reg.B5W;
import model.pumping.reg.B5Wmod;
import model.pumping.reg.BBABAnAn;
import model.pumping.reg.BkABnBAn;
import model.pumping.reg.NaNb;
import model.pumping.reg.Palindrome;


/**
 * Factory to produce the correct PumpingLemma instance from a saved file.
 * 
 * @author Jinghui Lim
 *
 */
public class PumpingLemmaFactory 
{
    public static PumpingLemma createPumpingLemma(String type, String name)
    {
        if(type.equals(RegPumpingLemmaTransducer.REG_LEMMA_TAG))
        {
            if(name.equals(new ABnAk().getName()))
                return new ABnAk();
            else if(name.equals(new AnBkCnk().getName()))
                return new AnBkCnk();
            else if(name.equals(new AnBlAk().getName()))
                return new AnBlAk();
            else if(name.equals(new AnBn().getName()))
                return new AnBn();
            else if(name.equals(new AnEven().getName()))
                return new AnEven();
            else if(name.equals(new NaNb().getName()))
                return new NaNb();
            else if(name.equals(new Palindrome().getName()))
                return new Palindrome();
            
            else if (name.equals(new BBABAnAn().getName()))
            	return new BBABAnAn();
            else if (name.equals(new B5W().getName()))
            	return new B5W();
            else if (name.equals(new BkABnBAn().getName()))
            	return new BkABnBAn();
            else if (name.equals(new AnBk().getName()))
            	return new AnBk();
            else if (name.equals(new AB2n().getName()))
            	return new AB2n();
            else if (name.equals(new B5Wmod().getName()))
            	return new B5Wmod();            
            else    // this should not happen 
                return null;
        }
        else if(type.equals(CFPumpingLemmaTransducer.CF_LEMMA_TAG))
        {
            if(name.equals(new AnBjAnBj().getName()))
                return new AnBjAnBj();
            else if(name.equals(new AiBjCk().getName()))
                return new AiBjCk();
            else if(name.equals(new model.pumping.cf.AnBn().getName()))
                return new model.pumping.cf.AnBn();
            else if(name.equals(new AnBnCn().getName()))
                return new AnBnCn();
            else if(name.equals(new NagNbeNc().getName()))
                return new NagNbeNc();
            else if(name.equals(new NaNbNc().getName()))
                return new NaNbNc();
            else if(name.equals(new WW().getName()))
                return new WW();
            
            else if (name.equals(new WW1WrEquals().getName()))
            	return new WW1WrEquals();
            else if (name.equals(new W1BnW2().getName()))
            	return new W1BnW2();
            else if (name.equals(new W1CW2CW3CW4().getName()))
            	return new W1CW2CW3CW4();
            else if (name.equals(new WW1WrGrtrThanEq().getName()))
            	return new WW1WrGrtrThanEq();
            else if (name.equals(new AkBnCnDj().getName()))
            	return new AkBnCnDj();
            else if (name.equals(new W1VVrW2().getName()))
            	return new W1VVrW2();  
            else    // this shouldn't happen
                return null;
        }
        else    // this shouldn't happen
            return null;
    }
}