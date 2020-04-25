package com.company;


public class Main {

    public static void main(String[] args) {

        Polynomial p1, p2, p3, p4;
        p1 = new Polynomial(-1,-2);
        p2 = new Polynomial(0,0,-1);
        p3 = new Polynomial(1, 2, -1);
        p4 = new Polynomial(2, 4, 3);

        p1.show();
        p2.show();
        p3.show();
        p4.show();
        (p3.add(p1)).show();
        (p3.add(p2)).show();

        System.out.println();

        p3.show();
        p4.show();
        System.out.println();

        (p1.multiply(p2)).show();
        (p4.multiply(p3)).show();

        System.out.println();

        Polynomial.getPolynomialWithRoots(1, -1).power(30).show();

        System.out.println();

        Polynomial p5 = (new Polynomial(1,2,4,3, 1, 2));
        for(int i = 0; i < p5.size() + 1; i++){
            p5.derivative(i).show();
        }

        System.out.println();

        Polynomial p6 = new Polynomial(1,1);

        p6.multiply(p6).show().multiply(new Polynomial(1,0,0,0,0,0,0,0,1)).show().add(-1).show();

    }
}
