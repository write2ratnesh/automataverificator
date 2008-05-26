/*
 * Developed by eVelopers Corporation - 26.05.2008
 */
package ru.ifmo.ltl.buchi.translator;

import ru.ifmo.ltl.buchi.ITranslator;
import ru.ifmo.ltl.buchi.IBuchiAutomata;
import ru.ifmo.ltl.buchi.ITransitionCondition;
import ru.ifmo.ltl.buchi.impl.BuchiNode;
import ru.ifmo.ltl.buchi.impl.BuchiAutomata;
import ru.ifmo.ltl.buchi.impl.TransitionCondition;
import ru.ifmo.ltl.grammar.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ltl2baTranslator implements ITranslator {
    private static final String PATH = "bin/ltl2ba.exe";
    private static final String ACCEPT_ALL = "accept_all";
    private static final String GOTO = "-> goto";

    private Map<String, IExpression<Boolean>> expr = new HashMap<String, IExpression<Boolean>>();
    private VisitorImpl visitor = new VisitorImpl();

    public IBuchiAutomata translate(LtlNode root) {
        try {
            String formula = getFormula(root);
            String automata = executeLlt2ba(formula);

            return extractBuchi(automata);
        } catch (Exception e) {
            throw new TranslationException(e);
        }
    }

    protected IBuchiAutomata extractBuchi(String automata) {
        BuchiAutomata buchi = new BuchiAutomata();
        String init = getInitState(automata);
        List<String> states = getStates(automata);
        List<String> accept = getAcceptStates(automata);
        int idSeq = 0;

        Map<String, BuchiNode> map = new HashMap<String, BuchiNode>();
        map.put(extractName(init), new BuchiNode(idSeq++));
        for (String str: states) {
            map.put(extractName(str), new BuchiNode(idSeq++));
        }
        Set<BuchiNode> acceptSet = new HashSet<BuchiNode>();
        for (String str: accept) {
            BuchiNode bNode = new BuchiNode(idSeq++);
            map.put(extractName(str), bNode);
            acceptSet.add(bNode);
        }
        map.put(ACCEPT_ALL, new BuchiNode(idSeq++));

        buchi.setStartNode(map.get(extractName(init)));
        buchi.addNodes(map.values());

        buchi.addAcceptSet(acceptSet);

        parseTransitions(map, init);
        for (String str: states) {
            parseTransitions(map, str);
        }
        for (String str: accept) {
            parseTransitions(map, str);
        }
        
        //create transition from accept_all to accept_all
        TransitionCondition cond = new TransitionCondition();
        cond.addExpression(BooleanNode.TRUE);
        BuchiNode acceptAll = map.get(ACCEPT_ALL);
        acceptAll.addTransition(cond, acceptAll);

        return buchi;
    }

    protected void parseTransitions(Map<String, BuchiNode> map, String state) {
        BuchiNode node = map.get(extractName(state));

        Matcher m = Pattern.compile("::.*").matcher(state);
        List<String> trans = findAll(m, state);
        for (String t: trans) {
            m = Pattern.compile("(.*)").matcher(t);
            if (m.find()) {
                ITransitionCondition cond = extractCondition(t.substring(m.start() + 1, m.end() - 1));
                int i = t.indexOf(GOTO);
                if (i < 0) {
                    throw new TranslationException("Unexpected transition format: " + t);
                } else {
                    String stateName = t.substring(i + GOTO.length());
                    node.addTransition(cond, map.get(stateName));
                }
            } else {
                throw new TranslationException("Unexpected transition format: " + t);
            }
        }
    }

    protected ITransitionCondition extractCondition(String condStr) {
        TransitionCondition cond = new TransitionCondition();
        String[] arr = condStr.split("&&");

        for (String c: arr) {
            if (c.charAt(0) == '!') {
                cond.addNegExpression(expr.get(c.substring(1)));
            } else {
                cond.addExpression(expr.get(c));
            }
        }
        return cond;
    }

    protected String extractName(String state) {
        return state.substring(0, state.indexOf(':'));
    }

    protected String getInitState(String automata) {
        Matcher m = Pattern.compile("T0_init:\\s+if(\\s+::.*)+\\s+fi;").matcher(automata);
        return (m.find()) ? automata.substring(m.start(), m.end()) : null;
    }

    protected List<String> getStates(String automata) {
        Matcher m = Pattern.compile("T\\d+_S\\d+:\\s+if(\\s+::.*)+\\s+fi;").matcher(automata);
        return findAll(m, automata);
    }

    protected List<String> getAcceptStates(String automata) {
        Matcher m = Pattern.compile("accept_\\w+:\\s+if(\\s+::.*)+\\s+fi;").matcher(automata);
        return findAll(m, automata);
    }

    protected List<String> findAll(Matcher m, String automata) {
        List<String> res = new ArrayList<String>();
        while (m.find()) {
            res.add(automata.substring(m.start(), m.end()));
        }
        return res;
    }

    public String executeLlt2ba(String formula) throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec(PATH + " -f \"" + formula + "\"");
        StreamReader reader = new StreamReader(proc.getInputStream());

        reader.start();
        proc.waitFor();
        reader.join();

        return reader.getResult();
    }

    public String getFormula(LtlNode root) {
        StringBuilder buf = new StringBuilder();
        convert(root, buf);
        return buf.toString();
    }

    private void convert(LtlNode node, StringBuilder buf) {
        if (node instanceof UnaryOperator) {
            buf.append(node.accept(visitor, null)).append('(');
            convert(((UnaryOperator) node).getOperand(), buf);
            buf.append(')');
        } else if (node instanceof BinaryOperator) {
            BinaryOperator op = (BinaryOperator) node;
            buf.append('(');
            convert(op.getLeftOperand(), buf);
            buf.append(')');
            buf.append(node.accept(visitor, null));
            buf.append('(');
            convert(op.getRightOperand(), buf);
            buf.append(')');
        } else if (node instanceof IExpression) {
            String tmp = node.accept(visitor, null);

            expr.put(tmp, (IExpression<Boolean>) node);
            buf.append(tmp);
        } else {
            throw new IllegalArgumentException("Unexpected node type: " + node.getClass());
        }
    }

    private class StreamReader extends Thread {
        private InputStream is;
        private StringWriter sw;

        StreamReader(InputStream is) {
            this.is = is;
            sw = new StringWriter();
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
                }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        String getResult() {
            return sw.toString();
        }
    }

    /**
     * Use Spin syntax
     */
    private class VisitorImpl implements INodeVisitor<String, Void> {

        public String visitPredicate(Predicate p, Void aVoid) {
            return p.getUniqueName();
        }

        public String visitNeg(UnaryOperator op, Void aVoid) {
            return "!";
        }

        public String visitFuture(UnaryOperator op, Void aVoid) {
            return "<>";
        }

        public String visitNext(UnaryOperator op, Void aVoid) {
            return "X";
        }

        public String visitAnd(BinaryOperator op, Void aVoid) {
            return "&&";
        }

        public String visitOr(BinaryOperator op, Void aVoid) {
            return "||";
        }

        public String visitRelease(BinaryOperator op, Void aVoid) {
            return "V";
        }

        public String visitUntil(BinaryOperator op, Void aVoid) {
            return "U";
        }

        public String visitGlobal(UnaryOperator op, Void aVoid) {
            return "[]";
        }

        public String visitBoolean(BooleanNode b, Void aVoid) {
            return b.getValue().toString();
        }
    }
}
