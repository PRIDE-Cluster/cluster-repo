package uk.ac.ebi.pride.spectracluster.repo.utils;

import uk.ac.ebi.pridemod.controller.DataAccessController;
import uk.ac.ebi.pridemod.controller.impl.PSIModDataAccessController;
import uk.ac.ebi.pridemod.controller.impl.UnimodDataAccessController;
import uk.ac.ebi.pridemod.model.PTM;
import uk.ac.ebi.pridemod.model.Specificity;

import java.io.InputStream;
import java.util.List;

/**
 * ModificationDetailFetcher fetches modification details from PSI-MOD and UniMod ontology
 *
 * @author Rui Wang
 * @version $Id$
 */
public final class ModificationDetailFetcher implements DataAccessController{

    private final PSIModDataAccessController psiModDataAccessController;
    private final UnimodDataAccessController unimodDataAccessController;

    public ModificationDetailFetcher(InputStream psiModFile, InputStream uniModFile) {
        psiModDataAccessController = new PSIModDataAccessController(psiModFile);
        unimodDataAccessController = new UnimodDataAccessController(uniModFile);
    }

    @Override
    public InputStream getSource() {
        throw new UnsupportedOperationException("getSource is not supported by this aggregated class");
    }

    @Override
    public PTM getPTMbyAccession(String s) {
        if (s != null) {
            PTM ptm = psiModDataAccessController.getPTMbyAccession(s);
            if (ptm == null) {
                ptm = unimodDataAccessController.getPTMbyAccession(s);
            }
            return ptm;
        }
        return null;
    }

    @Override
    public List<PTM> getPTMListByPatternName(String s) {
        if (s != null) {
            List<PTM> ptms = psiModDataAccessController.getPTMListByPatternName(s);
            if (ptms == null || ptms.isEmpty()) {
                ptms = unimodDataAccessController.getPTMListByPatternName(s);
            }
            return ptms;
        }
        return null;
    }

    @Override
    public List<PTM> getPTMListBySpecificity(Specificity specificity) {
        if (specificity != null) {
            List<PTM> ptms = psiModDataAccessController.getPTMListBySpecificity(specificity);
            if (ptms == null || ptms.isEmpty()) {
                ptms = unimodDataAccessController.getPTMListBySpecificity(specificity);
            }
            return ptms;
        }
        return null;
    }

    @Override
    public List<PTM> getPTMListByPatternDescription(String s) {
        if (s != null) {
            List<PTM> ptms = psiModDataAccessController.getPTMListByPatternDescription(s);
            if (ptms == null || ptms.isEmpty()) {
                ptms = unimodDataAccessController.getPTMListByPatternDescription(s);
            }
            return ptms;
        }
        return null;
    }

    @Override
    public List<PTM> getPTMListByEqualName(String s) {
        if (s != null) {
            List<PTM> ptms = psiModDataAccessController.getPTMListByEqualName(s);
            if (ptms == null || ptms.isEmpty()) {
                ptms = unimodDataAccessController.getPTMListByEqualName(s);
            }
            return ptms;
        }
        return null;
    }

    @Override
    public List<PTM> getPTMListByMonoDeltaMass(Double aDouble) {
        if (aDouble != null) {
            List<PTM> ptms = psiModDataAccessController.getPTMListByMonoDeltaMass(aDouble);
            if (ptms == null || ptms.isEmpty()) {
                ptms = unimodDataAccessController.getPTMListByMonoDeltaMass(aDouble);
            }
            return ptms;
        }
        return null;
    }

    @Override
    public List<PTM> getPTMListByAvgDeltaMass(Double aDouble) {
        if (aDouble != null) {
            List<PTM> ptms = psiModDataAccessController.getPTMListByAvgDeltaMass(aDouble);
            if (ptms == null || ptms.isEmpty()) {
                ptms = unimodDataAccessController.getPTMListByAvgDeltaMass(aDouble);
            }
            return ptms;
        }
        return null;
    }
}
