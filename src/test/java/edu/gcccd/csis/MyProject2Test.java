package edu.gcccd.csis;

import org.junit.Test;

import java.io.*;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class MyProject2Test {
    private static BigInteger genBigInteger(final NodeList<Integer> nodeList) {
        final StringBuilder sb = new StringBuilder();
        for (final int i : nodeList) {
            sb.append(i);
        }
        return new BigInteger(sb.toString());
    }

    private static NodeList<Integer> genNodeList(final String s) { // "100" .. '1','0','0'
        final NodeList<Integer> nodeList = new NodeList<>();
        for (final char c : s.toCharArray()) {
            nodeList.append(Character.getNumericValue(c)); // '0' ..'9'
        }
        return nodeList;
    }


     //Adding two long positive integer values
    @Test
    public void testAddition() {

        final NodeList<Integer> n1 = Project2.generateNumber(50);
        final NodeList<Integer> n2 = Project2.generateNumber(50);

        final BigInteger N1 = genBigInteger(n1);
        final BigInteger N2 = genBigInteger(n2);

        final NodeList<Integer> n3 = new MyProject2().addition(n1, n2);
        final BigInteger N3 = N1.add(N2);

        assertEquals(N3, genBigInteger(n3));


        // corner cases
        NodeList<Integer> n4 = genNodeList("001");
        NodeList<Integer> n5 = genNodeList("300");

        NodeList<Integer> n6 = new MyProject2().addition(n4, n5);
        assertEquals(new BigInteger("301"), genBigInteger(n6));

        // no leading 0s
        assertEquals(3, n6.getLength());

        // many zeros
        n4 = genNodeList("000");
        n5 = genNodeList("000");
        n6 = new MyProject2().addition(n4,n5);
        assertEquals(1,n6.getLength());
        assertEquals(new BigInteger("0"),genBigInteger(n6));

        // empty lists
        n4 = new NodeList<>();
        n5 = new NodeList<>();
        n6 = new MyProject2().addition(n4, n5);

        // 0 + 0 = 0
        n4 = genNodeList("0");
        n5 = genNodeList("0");
        n6 = new MyProject2().addition(n4, n5);
        assertEquals(1, n6.getLength());
        assertEquals(new BigInteger("0"), genBigInteger(n6));

        // 0 + empty list  = 0
        n5 = new NodeList<>();
        n6 = new MyProject2().addition(n4, n5);
        assertEquals(1, n6.getLength());
        assertEquals(new BigInteger("0"), genBigInteger(n6));

        // empty list + 0  = 0
        n6 = new MyProject2().addition(n5, n4);
        assertEquals(1, n6.getLength());
        assertEquals(new BigInteger("0"), genBigInteger(n6));

    }

    /*
     * Adding several long integer values
     */
    @Test
    public void testAdditionIterator() {
        NodeList<NodeList<Integer>> list = new NodeList<>(); // 80 nodelist containing 123456789
        for (int i = 0; i < 80; i++) {
            list.append(genNodeList("123456789"));
        }
        NodeList<Integer> result = new MyProject2().addition(list.iterator());

        assertEquals(new BigInteger("9876543120"), genBigInteger(result));

        //5 nodelist containing different size
        list = new NodeList<>();
        String s = "1";
        for (int i = 0; i < 5; i++) {
            s += i;
            list.append(genNodeList(s));
        }

        result = new MyProject2().addition(list.iterator());
        assertEquals(new BigInteger("112480"), genBigInteger(result));


        //50 nodelist containing 0
        list = new NodeList<>();
        for (int i = 0; i < 50; i++) {
            list.append(genNodeList("0"));
        }

        result = new MyProject2().addition(list.iterator());
        assertEquals(new BigInteger("0"), genBigInteger(result));

        //50 nodelist leading zeros
        list = new NodeList<>();
        for (int i = 0; i < 50; i++) {
            list.append(genNodeList("0001"));
        }

        result = new MyProject2().addition(list.iterator());
        assertEquals(new BigInteger("50"), genBigInteger(result));

        //50 nodelist many zeros
        list = new NodeList<>();
        for (int i = 0; i < 50; i++) {
            list.append(genNodeList("000"));
        }

        result = new MyProject2().addition(list.iterator());
        assertEquals(new BigInteger("0"), genBigInteger(result));

        //nodelist empty
        list = new NodeList<>();
        result = new MyProject2().addition(list.iterator());

    }

    @Test
    public void saveTest() {
        final NodeList<Integer> n1 = genNodeList("123456789");
        final NodeList<Integer> n2 = genNodeList("123456789");

        final BigInteger N1 = genBigInteger(n1);
        final BigInteger N2 = genBigInteger(n2);

        final NodeList<Integer> n3 = new MyProject2().addition(n1, n2);
        final BigInteger N3 = N1.add(N2);

        //file exists
        String fileName = "test";
        new MyProject2().save(n3, fileName);
        File shouldExist = new File(fileName);
        assertTrue(shouldExist.exists());

        //content in file was saved correctly
        NodeList<Integer> n4 = new MyProject2().load(fileName);
        assertEquals(N3, genBigInteger(n4));

        //content is empty
        new MyProject2().save(new NodeList<>(), fileName);
        n4 = new MyProject2().load(fileName);
        assertTrue(n4.getLength() == 0);

        //file name is empty, file doesnt exist
        fileName = "";
        new MyProject2().save(n3, fileName);
        shouldExist = new File(fileName);
        assertFalse(shouldExist.exists());

    }

    @Test
    public void loadTest() {
        final NodeList<Integer> n1 = genNodeList("123456789");
        final NodeList<Integer> n2 = genNodeList("123456789");

        final BigInteger N1 = genBigInteger(n1);
        final BigInteger N2 = genBigInteger(n2);

        final NodeList<Integer> n3 = new MyProject2().addition(n1, n2);
        final BigInteger N3 = N1.add(N2);

        //file exists
        String fileName = "test";
        new MyProject2().load(fileName);
        File shouldExist = new File(fileName);
        assertTrue(shouldExist.exists());

        //content in file loaded correctly
        new MyProject2().save(n3, fileName);
        NodeList<Integer> n4 = new MyProject2().load(fileName);
        assertEquals(N3, genBigInteger(n4));

        //content is empty nothing gets loaded
        new MyProject2().save(new NodeList<>(), fileName);
        n4 = new MyProject2().load(fileName);
        assertTrue(n4.getLength() == 0);

        //file name is empty, file doesnt exist
        fileName = "";
        new MyProject2().load(fileName);
        shouldExist = new File(fileName);
        assertFalse(shouldExist.exists());

    }
}
