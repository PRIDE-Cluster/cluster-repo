package uk.ac.ebi.pride.spectracluster.repo.dao.library;

import uk.ac.ebi.pride.spectracluster.repo.model.SpectralLibraryDetail;

import java.util.List;

/**
 * Write interface for spectral library details
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface ISpectralLibraryWriteDao {

    /**
     * Save a list of spectral library details
     *
     * @param spectralLibraryDetails a given list of spectral library details
     */
    void saveSpectralLibraries(List<SpectralLibraryDetail> spectralLibraryDetails);

    /**
     * Save a single spectral library detail
     *
     * @param spectralLibraryDetail a given spectral library
     */
    void saveSpectralLibrary(SpectralLibraryDetail spectralLibraryDetail);
}
