package tesla;



import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {

    private final Repository repository;

    public Controller(Repository repository) {
        this.repository = repository;
    }

    @PostMapping("/models")
    @ResponseStatus(HttpStatus.CREATED)
    public Tesla saveCar(@RequestBody Tesla car) {
        return repository.save(car);
    }

    @GetMapping("/models")
    @ResponseStatus(HttpStatus.OK)
    public List<Tesla> getAllCars() {
        return repository.findAll();
    }

    @GetMapping("/models/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Tesla getCarById(@PathVariable Integer id) {

        Tesla car = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found with id = " + id));

        return car;
    }

    @PutMapping("/models/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Tesla refreshCar(@PathVariable("id") Integer id, @RequestBody Tesla car) {

        return repository.findById(id)
                .map(entity -> {
                    entity.setName(car.getName());
                    entity.setYear(car.getYear());
                    entity.setPrice(car.getPrice());
                    return repository.save(entity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Car not found with id = " + id));
    }

    @DeleteMapping("/models/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCarById(@PathVariable Integer id) {
        Tesla car = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found with id = " + id));
        repository.delete(car);
    }

    @DeleteMapping("/models")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAllCars() {
        repository.deleteAll();
    }
}
