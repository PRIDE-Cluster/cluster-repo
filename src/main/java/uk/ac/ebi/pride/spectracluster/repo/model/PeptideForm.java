package uk.ac.ebi.pride.spectracluster.repo.model;

import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 23/03/2016
 */
public class PeptideForm implements Comparable{

    String sequence;

    List<ModificationProvider> modifications;

    public PeptideForm(String sequence, List<ModificationProvider> modifications) {
        this.sequence = sequence;
        this.modifications = modifications;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public List<ModificationProvider> getModifications() {
        return modifications;
    }

    public void setModifications(List<ModificationProvider> modifications) {
        this.modifications = modifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeptideForm)) return false;
        PeptideForm that = (PeptideForm) o;
        if(!sequence.equals(that.sequence)) return false;
        return !(modifications != null ? !equalsMod(modifications, that.modifications) : that.modifications != null);

    }

    private boolean equalsMod(List<ModificationProvider> modifications, List<ModificationProvider> modifications1){
        if(modifications1 == null)
            return false;
        Set<String> mods = new TreeSet<String>();
        for(ModificationProvider mod: modifications)
            for(Integer pos: mod.getPositionMap().keySet())
                mods.add(pos + "-"+ mod.getAccession());
        Set<String> mods1 = new TreeSet<String>();
        for(ModificationProvider mod: modifications1)
            for(Integer pos: mod.getPositionMap().keySet())
                mods1.add(pos + "-"+ mod.getAccession());
        return mods.equals(mods1);
    }

    private boolean compareSequenceEqual(String sequence, String sequence1) {
        if(sequence.length() != sequence1.length())
            return false;
        char[] sequenceArray = sequence.toCharArray();
        char[] sequence1Array = sequence1.toCharArray();
        for(int i = 0; i < sequence.length(); i++)
            if(!compareAmminoAcid(sequenceArray[i], sequence1Array[i]))
                return false;
        return true;

    }

    private boolean compareAmminoAcid(char c, char c1) {
        if(c == '*' || c1 == '*')
            return true;
        if((c == 'L' && c1 == 'I') || (c == 'I' && c1 == 'L'))
            return true;
        return (Character.compare(c, c1) == 0);
    }

    @Override
    public int hashCode() {
        int result = sequence.hashCode();
        result = 31 * result + (modifications != null ? modificationHashCode() : 0);
        return result;
    }

    private int modificationHashCode(){
        int result = 0;
        for(ModificationProvider mod: modifications){
            for(Integer position: mod.getPositionMap().keySet())
                result = 31 * result + 31 * mod.getAccession().hashCode() + 31 * position;
        }
        return result;
    }

    @Override
    public String toString() {
        return "PeptideForm{" +
                "sequence='" + sequence + '\'' +
                ", modifications=" + modifications +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if(sequence.compareToIgnoreCase(((PeptideForm)o).getSequence()) !=0)
            return sequence.compareTo(((PeptideForm)o).getSequence());
        else
            return compareMods(modifications, ((PeptideForm)o).getModifications());
    }

    private int compareMods(List<ModificationProvider> modifications, List<ModificationProvider> modifications1) {
        Set<String> mods = new TreeSet<String>();
        if(modifications != null){
            for(ModificationProvider mod: modifications)
                for(Integer pos: mod.getPositionMap().keySet())
                    mods.add(pos + "-"+ mod.getAccession());
        }
        Set<String> mods1 = new TreeSet<String>();
        if(modifications1 != null){
            for(ModificationProvider mod: modifications1)
                for(Integer pos: mod.getPositionMap().keySet())
                    mods1.add(pos + "-"+ mod.getAccession());
        }
        return mods.toString().compareTo(mods1.toString());
    }


}
