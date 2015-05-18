/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containers;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jumpalottahigh
 */
public class ContainerHistory {
    private List<Double> C_list;
    
    public ContainerHistory() {
        this.C_list = new ArrayList<Double>();
        
    }

    public void add(double situation) {
        this.C_list.add(situation);
    }

    public void reset() {
        this.C_list.removeAll(C_list);
    }

    public String toString() {
        return this.C_list.toString();
    }

    public double maxValue() {
        if (this.C_list.isEmpty()) {
            return 0.0;
        } else {
            double max = this.C_list.get(0);
            for (double temp : this.C_list) {
                if (temp > max) {
                    max = temp;
                }
            }

            return max;
        }
    }

    public double minValue() {
        if (this.C_list.isEmpty()) {
            return 0.0;
        } else {
            double min = this.C_list.get(0);
            for (double temp : this.C_list) {
                if (temp < min) {
                    min = temp;
                }
            }

            return min;
        }
    }

    public double average() {
        if (this.C_list.isEmpty()) {
            return 0.0;
        } else {
            double sum = 0;
            for (double temp : this.C_list) {
                sum += temp;
            }
            return sum / (double) this.C_list.size();
        }
    }

    public double greatestFluctuation() {
        if (this.C_list.isEmpty() || this.C_list.size() == 1) {
            return 0.0;
        } else {
            double max = 0;
            int i=0;
            for(double temp: this.C_list){
                double temp1 = this.C_list.get(i+1);
                double max_temp = Math.abs(temp-temp1);
                if(max_temp>max){
                    max = max_temp;
                }
                
                i++;
                if(i>=this.C_list.size()-1){
                    break;
                }
            }
            return max;
        }
    }

    public double variance() {

        if (this.C_list.isEmpty() || this.C_list.size() == 1) {
            return 0.0;
        } else {
            double mean = this.average();
            double temp = 0;
            for (double a : this.C_list) {
                temp += (a - mean) * (a - mean);
                
            }
            return temp / (this.C_list.size()-1);
        }
        
    }
}
