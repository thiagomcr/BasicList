package edu.gcccd.csis;

import java.io.*;
import java.util.Iterator;

public class MyProject2 implements Project2 {

    NodeList<Integer> tempNode;

    @Override
    public NodeList<Integer> addition(NodeList<Integer> nodeList1, NodeList<Integer> nodeList2) {
        NodeList<Integer> sum = new NodeList<>();

        //Remove leading zeros
        nodeList1 = removeLeadingZeros(nodeList1);
        nodeList2 = removeLeadingZeros(nodeList2);

        //Reverse the elements
        nodeList1 = reverse(nodeList1.iterator());
        nodeList2 = reverse(nodeList2.iterator());

        Iterator<Integer> biggerNodeIterator;
        Iterator<Integer> smallerNodeIterator;
        if(nodeList1.getLength() >= nodeList2.getLength()){
            biggerNodeIterator = nodeList1.iterator();
            smallerNodeIterator = nodeList2.iterator();
        } else {
            smallerNodeIterator = nodeList1.iterator();
            biggerNodeIterator = nodeList2.iterator();
        }

        //Add up two nodes
        int quotient = 0;
        int number = 0;
        while (biggerNodeIterator.hasNext()) {

            if(smallerNodeIterator.hasNext()){
                number = smallerNodeIterator.next();
            } else {
                number = 0;
            }

            int sumNum = quotient + biggerNodeIterator.next() + number;
            int remainder;

            if (sumNum > 9) {
                remainder = sumNum % 10;
                quotient = 1;

            } else {
                remainder = sumNum;
                quotient = 0;
            }

            //append the sum of two elements
            sum.append(remainder);
        }
        //if last numbers were greater than 10 it will add 1 to end of the node
        if(quotient == 1){
            sum.append(quotient);
        }
        sum = reverse(sum.iterator());
        return sum;
    }

    /*Big O for this method: Iterator.next method gets called depending on the number of nodelist's elements.
     * therefore the complexity is O(n). Where n is the number of elements in the largest nodelist.
     */
    @Override
    public NodeList<Integer> addition(Iterator<NodeList<Integer>> iterator){
        NodeList<Integer> sum = new NodeList<>();

        while(iterator.hasNext()) {
            NodeList<Integer> i = iterator.next();
            sum = addition(i, sum);
        }
        return sum;
    }

    /*If the List has 5 items Ex: 12345
     * And we want it to reverse it to 54321
     * The provided iterator's next method gets called 5 times, where 5 is the number of items in the list.
     * Thus O(n).
     */
    public NodeList<Integer> reverse(Iterator<Integer> iterator){
        tempNode = new NodeList<>();

        while (iterator.hasNext()){
            final int i = iterator.next();
            reverse(iterator);
            tempNode.append(i);
        }

        return tempNode;
    }

    public NodeList<Integer> removeLeadingZeros(NodeList<Integer> nodeList){
        tempNode = new NodeList<>();

        //count how many leading zeros
        Iterator<Integer> it = nodeList.iterator();
        int i = 0;
        while (it.hasNext() && it.next() == 0 && nodeList.getLength() != 1) {
            i++;
        }

        //no leading zeros return same list
        if(i == 0 ){ return nodeList; }

        it = nodeList.iterator();

        //has leading zeros check and check if they are all zeros
        int j = (i == nodeList.getLength() ? i : 0);
        while(it.hasNext()){
            if(j >= i && j <= nodeList.getLength()){
                final int k = it.next();
                tempNode.append(k);
                j++;
            } else {
                it.next();
                j++;
            }
        }
        return tempNode;
    }

    @Override
    public void save(NodeList<Integer> nodeList, String fileName) {
        if(fileName != "") {
            try {
                final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));

                for (Integer value : nodeList) {
                    dos.write(value);
                }
                dos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public NodeList<Integer> load(String fileName) {
        NodeList<Integer> nodeList = new NodeList<>();

        if(fileName != "") {
            try {
                final DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));

                while (dis.available() > 0) {
                    nodeList.append(dis.read());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nodeList;
    }


    public static void main(final String[] args) {
        final int L = 30;

        final NodeList<Integer> n1 = Project2.generateNumber(L); // (head 1st) e.g. 3457
        final NodeList<Integer> n2 = Project2.generateNumber(L); // (head 1st) e.g. 682

        final Project2 p = new MyProject2();

        Project2.print(p.addition(n1, n2)); //  n1+n2, e.g. 4139

        final NodeList<NodeList<Integer>> listOfLists = new NodeList<>();
        for (int i = 0; i < L; i++) {
            listOfLists.append(Project2.generateNumber(L));
        }
        p.save(p.addition(listOfLists.iterator()), "result.bin");
        Project2.print(p.load("result.bin"));
    }
}
