package com.company;

import com.sun.prism.null3d.NULL3DPipeline;

import java.security.PublicKey;
import java.util.*;
import java.util.function.DoubleBinaryOperator;

public class Polynomial {

    private Vector<Number> coeffs;
    private char argument;

    public Polynomial(){
        coeffs = new Vector<Number>();
        argument = 'x';
    }

    public Polynomial(Polynomial toCopy){
        coeffs = new Vector<Number>();
        argument = 'x';
        this.copy(toCopy);
    }

    public Polynomial(Number ... coefficients){
        coeffs = new Vector<Number>();
        argument = 'x';
        int i = 0;
        for(Number coeff: coefficients){
            setCoeff(coeff, i);
            i++;
        }
    }

    Polynomial(Integer ... args){
        coeffs = new Vector<Number>();
        argument = 'x';
        int i = 0;
        for(Integer val: args){
            setCoeff(val.doubleValue(), i);
            i++;
        }
    }

    Polynomial(Collection<Number> colection){
        coeffs = new Vector<Number>();
        argument = 'x';
        this.copy(colection);
    }

    public Polynomial copy(){
        return new Polynomial(this);
    }

    public Polynomial copy(Polynomial p){
        this.setSize(p.size());
        for(int i = 0; i < p.size(); i++){
            this.setCoeff(p.getCoeff(i), i);
        }
        return this;
    }

    public void copy(Collection<Number> col){
        this.setSize(col.size());
        int i = 0;
        for(Number val: col){
            this.setCoeff(val, i);
            i++;
        }
    }

    public int size(){
        return coeffs.size();
    }

    public void setSize(int size){
        if(size < 0){
            coeffs.setSize(0);
            return;
        }

        if(size() < size){
            int oldSize = size();
            coeffs.setSize(size);
            for(int i = oldSize; i < size(); i++ ){
                coeffs.set(i, 0.0);
            }
        }

        if(size() > size){
            coeffs.setSize(size);
        }
    }

    public Number getCoeff(int i){
        if(i >= size()){
            setSize(i+1);
        }

        return coeffs.get(i);
    }


    public void setCoeff(Number value, int i){
        if(i >= size()){
            setSize(i+1);
        }
        coeffs.set(i, value);
        normalize();
    }

    public void addCoeff(Number value, int i){
        setCoeff(getCoeff(i).doubleValue() + value.doubleValue(), i);
    }

    public void multiplyCoeff(Number value, int i){
        setCoeff(getCoeff(i).doubleValue() * value.doubleValue(), i);
    }

    private void normalize(){
        int nonZero = size() -1;

        while(nonZero >= 0 && getCoeff(nonZero).doubleValue() == 0){
            nonZero--;
        }

        setSize(nonZero + 1);
    }

    public Double calculate(Number num){
        double x = num.doubleValue();
        if(size() == 0){
            return 0.0;
        }
        double result = coeffs.lastElement().doubleValue();
        for(int i = size()-2; i >= 0; i--){
            result = result*x + coeffs.get(i).doubleValue();
        }
        return result;
    }

    public Polynomial derivative(){
        return derivative(1);
    }

    public Polynomial derivative(Integer degree){
        Polynomial newPol = new Polynomial();
        newPol.setSize(size() - degree);
        double value;
        for(int i = 0; i < size() - degree; i++){
            value = getCoeff(i + degree).doubleValue();
            for(int j = i; j < i + degree; j++){
                value *= j + 1;
            }
            newPol.setCoeff(value, i);
        }

        return newPol;
    }

    public static Polynomial getPolynomialWithRoots(Number leading, Number ... args){
        ArrayList<Number> roots = new ArrayList<Number>(Arrays.asList(args));

        Polynomial newPol = new Polynomial(leading);
        for(Number root: roots){
            newPol.multiply(new Polynomial(-root.doubleValue(), 1.0));
        }


        return  newPol;
    }

    public static Polynomial getPolynomialWithRoots(Number root){
        return getPolynomialWithRoots(1, root);
    }

    public Polynomial power(Integer n){
        int toPower = powerOf2SmallerThan(n);
        int rest = n;

        Vector<Polynomial> pols = new Vector<Polynomial>();
        pols.setSize(toPower+1);
        pols.set(0, this.copy());

        for(int i = 1; i <= toPower; i++){
            pols.set(i, pols.get(i-1).copy().multiply(pols.get(i-1)));
        }

        Polynomial newPol = new Polynomial(1);

        while(rest > 0){
            toPower = powerOf2SmallerThan(rest);
            rest = rest - (int) Math.pow(2, toPower);

            newPol.multiply(pols.get(toPower));
        }

        return newPol;
    }

    private int powerOf2SmallerThan(Integer num){
        return (int) (Math.log(num)/Math.log(2));
    }

    public Polynomial add(Number num){
        setCoeff(getCoeff(0).doubleValue() + num.doubleValue(), 0);
        return this;
    }


    public Polynomial add(Polynomial p){

        int max = (size() > p.size()) ? size(): p.size();

        this.setSize(max);

        for(int i = 0; i < max; i++){
            addCoeff(p.getCoeff(i).doubleValue(), i);
        }

        return this;
    }

    public Polynomial add(Collection<Number> collection){

        int max = (size() > collection.size()) ? size(): collection.size();

        int i = 0;
        for(Number val: collection){
            this.addCoeff(val.doubleValue(), i);
            i++;
        }

        return this;
    }

    public Polynomial subtract(Number num){

        this.addCoeff(-num.doubleValue(), 0);

        return this;
    }

    public Polynomial subtract(Polynomial p){

        int max = (size() > p.size())? size(): p.size();

        for(int i = 0; i < max; i++){
            addCoeff(-p.getCoeff(i).doubleValue(), i);
        }


        return this;
    }

    public Polynomial subtract(Collection<Number> collection){
        int max = (size() > collection.size()) ? size(): collection.size();
        setSize(max);
        int i = 0;
        for(Number val: collection){
            addCoeff(-val.doubleValue(), i);
            i++;
        }

        return this;
    }

    public Polynomial multiply(Number value){

        for(int i = 0; i < size(); i++){
            multiplyCoeff(value.doubleValue(), i);
        }

        return this;
    }

    public Polynomial multiply(Polynomial p){
        if(p.size() == 1){
            if(p.getCoeff(0).doubleValue() == 1){
                return this;
            } else {
                return this.multiply(p.getCoeff(0));
            }
        }

        if(this.size() == 1){
            if(this.getCoeff(0).doubleValue() == 1){
                return this.copy(p);
            } else {
                Number toMultiply = this.getCoeff(0);
                return this.copy(p).multiply(toMultiply);
            }
        }

        Polynomial first = this.copy();
        Polynomial second = p.copy();

        this.clear();

        for(int i = 0; i < first.size(); i++){
            for(int j = 0; j < second.size(); j++){
                addCoeff(first.getCoeff(i).doubleValue() * second.getCoeff(j).doubleValue(), i + j);
            }
        }

        return this;
    }

    public Polynomial clear(){
        this.setSize(0);
        return this;
    }

    public String toString(){
        if(size() == 0){
            return "0";
        }

        boolean first = true;

        String polString = "";
        int i = 0;
        Double coeff;
        for(Number number: coeffs){
            coeff = number.doubleValue();
            if(i != 0){
                if(coeff != 0){

                    if(!first) {
                        if (coeff < 0) {
                            polString += " - ";
                        } else {
                            polString += " + ";
                        }
                    } else{
                        if(coeff < 0){
                            polString += " -";
                        }
                        first = false;
                    }

                    if(coeff == coeff.intValue()){
                        if (coeff != 1 && coeff != -1){
                            polString += (int) Math.abs(coeff);
                        }

                    } else {
                        polString += Math.abs(coeff);
                    }
                    if(i > 0){
                        polString += argument;
                        if(i > 1){
                            polString += "^" + i;
                        }
                    }
                }
            } else {
                if( coeff != 0){
                    if(coeff == coeff.intValue()){
                        polString += coeff.intValue();

                    } else {
                        polString += coeff;
                    }
                    first = false;
                }
            }
            i++;
        }

        return polString;
    }

    public Polynomial show(){
        System.out.println(toString());
        return this;
    }

}
