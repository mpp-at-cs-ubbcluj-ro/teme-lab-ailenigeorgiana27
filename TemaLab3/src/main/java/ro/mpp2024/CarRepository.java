package ro.mpp2024;

import ro.mpp2024.Car;
import ro.mpp2024.Repository;

import java.util.List;

public interface CarRepository extends Repository<Integer, Car> {
    List<Car> findByManufacturer(String manufacturer);
    List<Car> findBetweenYears(int min, int max);
}
