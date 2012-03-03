package net.fortytwo.flow.rdf.ranking;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface Approximation<T, E extends Exception> {
    /**
     * 
     * @param cycles the number of cycles available
     * @return the number of cycles actually consumed
     * @throws E an implementation-specific exception
     */
    int compute(int cycles) throws E;
    
    T currentResult();
}
