
package application;

public interface Sensor {
    boolean isOn();  // returns true if the sensor is on
    void on();       // switches the sensor on
    void off();      // switches the sensor off
    int measure();  // returns the sensor reading if the sensor is on
                     // if the sensor is off, it throws an IllegalStateException
}
