ЛР №3 (Spring Boot + Java)
1. Модели (сущности БД)
Где находятся: src/main/java/ru/citytour/app/model/


// Excursion.java - базовая абстрактная сущность
@Entity
@Table(name = "excursions")
@Inheritance(strategy = InheritanceType.JOINED)  // Наследование через JOIN
public abstract class Excursion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int basePrice;
    private int baseDuration;
    
    @OneToMany(mappedBy = "excursion")
    private List<Booking> bookings;
    
    public abstract int calculateFinalPrice(int people);
    public abstract String getType();
}

// CityTour.java - наследует Excursion
@Entity
@Table(name = "city_tours")
public class CityTour extends Excursion {
    private String transport;  // пешая, автобус, метро
    private boolean meal;      // обед
    private boolean hasGuide;  // гид
}

// MuseumTour.java - наследует Excursion
@Entity
@Table(name = "museum_tours")
public class MuseumTour extends Excursion {
    private String museum;      // название музея
    private String guide;       // язык гида
    private boolean audio;      // аудиогид
    private boolean expertGuide; // эксперт-гид
}
Связи между таблицами:

excursions (родитель) - общие поля

city_tours (ребёнок) - специфические поля, FK на excursions.id

museum_tours (ребёнок) - специфические поля, FK на excursions.id

2. Подключение к БД
Где находится: src/main/resources/application.yml

yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/citytourdb
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update  # Автоматическое создание/обновление таблиц
    show-sql: true      # Показывать SQL запросы
Как работает: Hibernate автоматически создаёт таблицы по моделям при запуске.

3. Репозитории (доступ к БД)
Где находятся: src/main/java/ru/citytour/app/repository/


// ExcursionRepository.java
public interface ExcursionRepository extends JpaRepository<Excursion, Long> {
    // JpaRepository уже содержит: save(), findAll(), findById(), deleteById()
}
4. Сервисы (бизнес-логика)
Где находятся: src/main/java/ru/citytour/app/service/


// TourService.java
@Service
public class TourService {
    private final ExcursionRepository repository;
    private final TourMapper mapper;
    
    // Чтение всех экскурсий
    public List<Excursion> getAllTours() {
        return repository.findAll();
    }
    
    // Создание CityTour
    public Excursion saveCityTour(CityTourDto dto) {
        CityTour tour = mapper.toEntity(dto);
        return repository.save(tour);
    }
    
    // Создание MuseumTour
    public Excursion saveMuseumTour(MuseumTourDto dto) {
        MuseumTour tour = mapper.toEntity(dto);
        return repository.save(tour);
    }
    
    // Редактирование
    public Excursion findById(Long id) {
        return repository.findById(id).orElseThrow();
    }
    
    // Удаление
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
5. Контроллеры (обработка HTTP запросов)
Где находятся: src/main/java/ru/citytour/app/controller/


// TourController.java
@Controller
public class TourController {
    
    // GET - показать главную страницу
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tours", tourService.getAllTours());
        return "index";
    }
    
    // GET - показать форму добавления CityTour
    @GetMapping("/add-city")
    public String addCityForm(Model model) {
        model.addAttribute("cityTour", new CityTourDto());
        return "add-city";
    }
    
    // POST - сохранить CityTour
    @PostMapping("/add-city")
    public String addCity(@ModelAttribute CityTourDto cityTour) {
        tourService.saveCityTour(cityTour);
        return "redirect:/";
    }
    
    // POST - удалить
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        tourService.deleteById(id);
        return "redirect:/";
    }
}
6. DTO (Data Transfer Object)
Где находятся: src/main/java/ru/citytour/app/dto/


// CityTourDto.java
@Data
public class CityTourDto {
    private Long id;
    private String name;
    private int basePrice;
    private int baseDuration;
    private String transport;
    private boolean meal;
    private boolean hasGuide;
}
7. Маппер (преобразование DTO ↔ Entity)
Где находится: src/main/java/ru/citytour/app/mapper/

@Component
public class TourMapper {
    public CityTour toEntity(CityTourDto dto) {
        return new CityTour(dto.getName(), dto.getBasePrice(), 
            dto.getBaseDuration(), dto.getTransport(), 
            dto.isMeal(), dto.isHasGuide());
    }
    
    public CityTourDto toDto(CityTour tour) {
        CityTourDto dto = new CityTourDto();
        dto.setId(tour.getId());
        dto.setName(tour.getName());
        // ...
        return dto;
    }
}
8. Бизнес-правила (расчёт цены и длительности)
Где находится: src/main/java/ru/citytour/app/model/CityTour.java

public int calculateFinalPrice(int people) {
    int price = getBasePrice();
    
    if (transport.equals("автобус")) price += 100;
    if (meal) price += 150;
    if (hasGuide) price += price * 20 / 100;  // +20%
    
    return price * people;
}

public int getFinalDuration() {
    int duration = getBaseDuration();
    
    if (transport.equals("автобус")) duration += 1;
    if (meal) duration += 1;
    if (hasGuide) duration += 2;
    
    return duration;
}