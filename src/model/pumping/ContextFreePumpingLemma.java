/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package model.pumping;

import java.io.Serializable;

import universe.preferences.JFLAPPreferences;




/**
 * A <code>ContextFreePumpingLemma</code> is a <code>PumpingLemma</code> that 
 * handles the five string segments that <i>w</i> is broken into, <i>uvxyz</i>.
 * 
 * @author Jinghui Lim & Chris Morgan
 *
 */
public abstract class ContextFreePumpingLemma extends PumpingLemma implements Serializable, Cloneable
{
    protected String u;
    protected String v;
    protected String x;
    protected String y;
    protected String z;        
    
    /**
     * Returns segment <i>u</i> of the decomposition.
     * 
     * @return <i>u</i> of the decomposition
     */
    public String getU() 
    {
        return u;
    }

    /**
     * Returns segment <i>v</i> of the decomposition.
     * 
     * @return <i>v</i> of the decomposition
     */
    public String getV() 
    {
        return v;
    }

    /**
     * Returns segment <i>x</i> of the decomposition.
     * 
     * @return <i>x</i> of the decomposition
     */
    public String getX() 
    {
        return x;
    }

    /**
     * Returns segment <i>y</i> of the decomposition.
     * 
     * @return <i>y</i> of the decomposition
     */
    public String getY() 
    {
        return y;
    }

    /**
     * Returns segment <i>z</i> of the decomposition.
     * 
     * @return <i>z</i> of the decomposition
     */
    public String getZ() 
    {
        return z;
    }
    
    
    public String getDecompositionAsString()
    {		
		String[] s = new String[5];
		int counter = 0;
		for (int i=0; i<=3; i++) {
			s[i] = w.substring(counter, counter + myDecomposition[i]);
			counter += myDecomposition[i];
		}
		s[4] = w.substring(counter);
		
		for (int i=0; i<s.length; i++)
			if (s[i].length() == 0)
				s[i] = JFLAPPreferences.getEmptyString();    //lower case lambda
		
		
		return "U = " + s[0] + "; V = " + s[1] + "; X = " + s[2] + 
		       "; Y = " + s[3] + "; Z = " + s[4];
    }
    
    /**
     * Clears the information the user and program have set for  <i>m</i>, <i>w</i>,
     * <i>i</i>, <i>u</i>, <i>v</i>, <i>x</i>, <i>y</i>, and <i>z</i>. 
     */
    public void reset()
    {
        m = -1;
        i = -1;
        w = "";
        u = "";
        v = "";
        x = "";
        y = "";
        z = "";
    }
    
    /**
     * Chooses the decomposition, with the length of each part of the 
     * decomposition in the corresponding space of the input array, 
     * then chooses an acceptable <i>i</i>.
     * 
     * @see #setDecomposition(int[], int)
     * @param decomposition the array that contains the length of each part of the decomposition
     * @return <code>true</code> if this deocmposition is legal, <code>false</code> otherwise
     */
    public boolean setDecomposition(int[] decomposition)
    {
    	myDecomposition = decomposition;
        int uLength = decomposition[0];
        int vLength = decomposition[1];
        int xLength = decomposition[2];
        int yLength = decomposition[3];
        if(vLength + xLength + yLength > m || vLength + yLength < 1)
            return false;
        
        u = w.substring(0, uLength);
        v = w.substring(uLength, uLength + vLength);
        x = w.substring(uLength + vLength, uLength + vLength + xLength);
        y = w.substring(uLength + vLength + xLength, uLength + vLength + xLength + yLength);
        z = w.substring(uLength + vLength + xLength + yLength);
        
        return true;
    }
    
    /**
     * Sets <i>i</i> and chooses the decomposition, with the length of 
     * each part of the decomposition in the corresponding space of the 
     * input array. That is, |<i>u</i>| = <code>decomposition[0]</code>, 
     * |<i>v</i>| = <code>decomposition[1]</code>,
     * |<i>x</i>| = <code>decomposition[2]</code>, 
     * |<i>y</i>| = <code>decomposition[3]</code>. 
     * 
     * @param decomposition the array that contains the length of each part of the decomposition
     * @param num the number to set <i>i</i> to
     * @return <code>true</code> if this decomposition is legal and has not been tried
     * before, <code>false</code> otherwise
     */
    public boolean setDecomposition(int[] decomposition, int num)
    {
        i = num;
        return setDecomposition(decomposition);
    }
    
    /**
     * Returns the pumped string <i>uv<sup>i</sup>xy<sup>i</sup>z</i> 
     * according the the decomposition and choice of <i>i</i>.
     * 
     * @return the pumped string, <i>uv<sup>i</sup>xy<sup>i</sup>z</i>
     */
    public String createPumpedString()
    {
        return u + pumpString(v, getI()) + x + pumpString(y, getI()) + z;
    }
    
    /**
     * Adds the decomposition to the list that the user has done. If the case is not
     * a legal decomposition, the it returns <code>-1</code>. If the case has already
     * been done, it returns the index of the case. If it hasn't been done, it moves
     * the case to the "done" list and returns its position in the done list.
     * 
     * @param decomposition the decomposition we wish to add
     * @return <code>-1</code> if it is an illegal decomposition, the index of the
     * decomposition if it has already been done, or a number greater than or equal
     * to the total number of cases, which can be found by calling 
     * {@link PumpingLemma#numCasesTotal()}. 
     */
    public int addCase(int[] decomposition, int num)
    {
        /*
         * addCase(int[]) should only be called after chooseDecomposition(int[]), 
         * so it should be a legal decomposition and uvxyz should have been set. 
         * Nonetheless, we check here.
         */
        if(!setDecomposition(decomposition))
            return -1;
        /*
         * Check if this case has been done.
         */
        for(int j = 0; j < myDoneCases.size(); j++)
        {
            if(((Case)(myDoneCases.get(j))).isCase(v, y))
                return j;
        }
        /*
         * Since case has not been done, find the case and put it into the
         * "done" pile.
         */
        for(int j = 0; j < myAllCases.size(); j++)
        {
            Case c = (Case)myAllCases.get(j);
            if(c.isCase(v, y))
            {
                c.setI(num);
                c.setUserInput(decomposition);
                myDoneCases.add(c);
                return myAllCases.size();
            }
        }
        System.err.println("BUG FOUND: ContextFreePumpingLemma.addCase(int[], int)");
        return -1;
    }
    
    
    public boolean replaceCase(int[] decomposition, int num, int index)
    {
        Case c = (Case)myDoneCases.get(index);
        if(c.isCase(v, y))
        {
            c.setI(num);
            c.setUserInput(decomposition);
            return true;
        }
        return false;
    }
    
    /**
     * Chooses a random decomposition, with it randomly spread over the string.
     * |vy| >= 1 && |vxy| <= m;
     */
    private void chooseDecompositionWithoutCases() {
    	int temp, counter;
		int[] decomp = new int[4];
		counter = 0;
		temp = 0;
		
		//Some ugly code...
		decomp[0] = LemmaMath.fetchRandInt(0, w.length() - 1);
		counter += decomp[0];
		temp = Math.min(w.length() - counter, m);
		decomp[1] = LemmaMath.fetchRandInt(0, temp);
		if (decomp[1] == w.length() - counter) {
			decomp[2] = 0;
			decomp[3] = 0; 
		}
		else {			
			counter += decomp[1];
			temp = Math.min(w.length() - counter - 1, m-1);
			decomp[2] = LemmaMath.fetchRandInt(0, temp);
			counter += decomp[2];
			temp = Math.min(w.length() - counter, m - decomp[2]);			
			if (decomp[1] > 0)
				decomp[3] = LemmaMath.fetchRandInt(0, temp);
			else
				decomp[3] = LemmaMath.fetchRandInt(1, temp);
		}
		
		setDecomposition(decomp);
    }
    @Override
	public String getHTMLTitle() {
		return "<i>L</i> = {" + this.getSpecialHTML() + "} Context Free Pumping Lemma";
	}
    
	public abstract String getSpecialHTML();
	
    /**
     * Chooses a random context-free decomposition, ignoring cases.
     */
    public void chooseDecomposition()
    {
    	// Currently just chooses a decomposition without cases.  The code for choosing it 
    	// with cases was deleted, as it wasn't being used. Can be found commented out in JFLAP 7.0 source, 
    	//and can be added if desired.
    	chooseDecompositionWithoutCases();  	
    }    	
    
    public String toString(){
    	return getSpecialHTML() +"\n"+
    			"U: "+ u + "\n" +
    			"V: "+ v + "\n" +
    			"W: "+ w + "\n" +
    			"X: "+ x + "\n" +
    			"Y: "+ y + "\n" +
    			"Z: "+ z + "\n" ;
    }
}
