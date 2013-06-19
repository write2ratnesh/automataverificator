/* 
 * Developed by eVelopers Corporation, 2013
 */
package ru.ifmo.test.nf2sl;

/**
 * @author kegorov
 *         Date: 6/19/13
 */
public class NasaRuleEventProvider {
    /*
     * nfv5 - nfv5 input template
     * nfv9 - nfv9/ipfix input template
     * tmpl - internal template <exp_ip, src_addr, dst_addr, src_port, dst_port...>
     * t - timer >= 60s
     * nt - temer < 60s
     * c2_next - c2 has elements
     * c2_empty - c2 is empty
     */

    public static final String nfv5 = "nfv5";
    public static final String nfv9 = "nfv9";
    public static final String tmpl = "tmpl";
    public static final String t = "t";
    public static final String nt = "nt";
    public static final String c2_next = "c2_next";
    public static final String c2_empty = "c2_empty";
}
