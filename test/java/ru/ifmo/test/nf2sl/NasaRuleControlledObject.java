/* 
 * Developed by eVelopers Corporation, 2013
 */
package ru.ifmo.test.nf2sl;

import com.evelopers.unimod.runtime.context.StateMachineContext;

/**
 * @author kegorov
 *         Date: 6/19/13
 */
public class NasaRuleControlledObject {
    /*
     * ACTIONS:
     * mr1, mr2. - match rules (extract_field(...))
     * mc1, mc2 - collect
     * sl - send syslog
     * c1_exp - set current exporter
     * c1_clean - clean c1
     * c2_clean - clean c2
     * t_reset - reset timer
     */


    public void mr1(StateMachineContext c) {
    }

    public void mr2(StateMachineContext c) {
    }

    public void mc1(StateMachineContext c) {
    }

    public void mc2(StateMachineContext c) {
    }

    public void sl(StateMachineContext c) {
    }

    public void c1_exp(StateMachineContext c) {
    }

    public void c1_clean(StateMachineContext c) {
    }

    public void c2_clean(StateMachineContext c) {
    }

    public void t_reset(StateMachineContext c) {
    }

}
