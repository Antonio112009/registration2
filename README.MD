Как я понимаю, для вашего проекта вы использовали что-то наподобии [данного ресурса](https://www.baeldung.com/registration-with-spring-mvc-and-spring-security).


## UserMVCController/RegistrationController

## Название файла
Так как контролер является частью страницы "регистрация", то лучше всего было бы назвать контроллер так же как и вашу страницу
с приставкой в конце "Controller". Это поможет вам не запутаться при вызове нужных вам классов в проекте.   
Ниже представлен пример как ЖЕЛАТЕЛЬНО должна выглядить папка "controller" (В простом проекте! В сложном немного подругому):
 - MainController
 - LoginController
 - FAQController
 - AboutController
 - RegistrationController   
 Просто и понятно)

### Ошибка в Get-method
Начнем с того, что у Вас не правильно записан GET-method. Глядя на вашу форму, можно заметить, что вы не передаете в форму объект "user",
в который будет идти вся запись данных с формы. 

Вот как выглядил Ваш код:
```java
@GetMapping("/registration")
       public ModelAndView registration()
       {
           return "registration";
       }
```

Вот как  предлагается на сайте baeldung.com:

```java
@RequestMapping(value = "/user/registration", method = RequestMethod.GET)
public String showRegistrationForm(WebRequest request, Model model) {
    
    //Две последующие строчки можно просто заменить model.addAttribute("user", new userDto()); 
    UserDto userDto = new UserDto();
    model.addAttribute("user", userDto);
    return "registration";
}
```

Но лично я предпочитаю так:
```java
@GetMapping("/registration")
    public ModelAndView registration()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }
```

В MAV мы передаем название страницы и объекты, которые мы хотим добавить на наше страницу.
Считаю, что данный способ является хорошим, так как отчетливо видно как вы говорите программе какой HTML-файл вызыть и
какие объекты вы передаете в этот HTML-файл.

### Ошибка в Post-method

Как у Вас это было:
```java
    @RequestMapping(path = "/createUser", method = RequestMethod.POST)
    public String createUser(User user)
    {
        service.createUser(user);
        return "redirect:/";
    }
```

Первая ошибка будет заключаться в `@RequestMapping`. Над вашим классом вы уже указали `@RequestMapping("/")`. Теперь внутри класса вы можете добавлять аннотации типа `@GetMapping`,
`@PostMapping` и т.д. Ваш вариант записи будет неверным. если вы хотите на одной странице 4+ метода , которые ссылаются  на разные страницы, то лучшей практикой будет убрать `@RequestMapping` над классом и прописывать его
у каждого метода. Вот  пример:

```java
@Controller
public class RegistrationController {

    @Autowired
    UsersService service;

    @GetMapping("/registration")
    public ModelAndView getRegistration()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute("user") User user)
    {
        service.createUser(user);
        return "redirect:/";
    }
    
    //... Некоторый код ...

    @PostMapping("/login")
    public ModelAndView getLogin(@ModelAttribute("user") User user)
        {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("login");
            modelAndView.addObject("user", new User());
            return modelAndView;
        }
}
```

Вторая ошибка (больше мой вариант решения) это название метода. Вместо `createUser`, я бы лучше назвал бы `postRegistration`

Третья ошибка... Я бы переделал код внутри метода `createUser`.    
Вот мой вариант решения:
```java
    @PostMapping("/registration/createUser")
    public ModelAndView postRegistration(@ModelAttribute("user") User user)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/registration");
        
        //Я бы тут добавил бы проверку, что юзера с таким, к примеру, именем и фамилией нету в БД

        registrationService.createUser(user);
        return modelAndView;
    }
```

### Правила хорошего тона
Вместо `User user`, я бы рекомендовал бы использовать `@ModelAttribute("user") User user`.


## UserService/RegistrationService

### Название файла
Как и в ситуации с контроллером, я бы назвал бы данный файл так же как страницу. Потому что все методы внутри данного сервиса связаны с регистрацией.
В случае, если какие-то методы в `Service` используются в нескольких контроллерах, то тогда бы можно было бы создать отдельный файл.
Напомню, что это лишь моя идея и у Вас есть вариант либо это учесть, либо отказаться от нее)

### Что-то там с Optional
Честно, не понял идеи следующих строк:
```java
        Optional<User> userOptional = repository.findById(user.getID_USER());

        User newUser= userOptional.get();
```

Если Вы создаете юзера, то создавайте его) Если хотите сделать валидацию (=проверку) того, что юзера нет в БД, то это лучше делать другим способом.
Пример как делать валидацию Вы найдете в [данной статье](https://www.baeldung.com/registration-with-spring-mvc-and-spring-security).

### Использование ненужных строк

Ваш код:
```java
        Optional<User> userOptional = repository.findById(user.getID_USER());

        User newUser= userOptional.get();
        newUser.setFirstName(user.getFirstName());
        newUser.setSecondName(user.getSecondName());
        newUser.setEmail(user.getEmail());
        newUser.setLogin(user.getLogin());
        newUser.setPassword(user.getPassword());
        newUser.setPhone(user.getPhone());
        newUser.setPhoto(user.getPhoto());
        newUser.setID_Role(user.getID_Role());

        newUser = repository.save(newUser);
```
Данные строчки не имеют смысла. Если вы хотите сохранить юзера - просто возьмите объект, который вы передаете в метод и с помощью  `.save(S s)` - сохраните его)
В ваш проект не добавлены еще зависимости Spring, связанные с Security, но помните, что сохранять пароль без шифровки в БД нельзя.
Один из вариантов шифрования - `BCrypt`. Данный алгоритм шифрования есть в `spring security`. 

Когда будете использовать шифрование, то в метод `createUser` добавьте строчку, которая шифрует пароль. Что-то в этом роде:
```java
user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
```


## Ошибки в HTML-файле

### Ошибка в \<form>
В форме Вы использовали 2 раза `action`. Точнее `action="#" th:action="@{/createUser}"`

### использование id в \<input>
`th:field="*{something}"` конвертируется в `id="something" name="something"`, поэтому отдельно id не имеет смысла прописывать

### Недостающий кусок в \<html>
в html-тэге стоит писать так:
```html
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      >
```


## Application.properties

### добавить порт
Когда я учил Спринг, меня напрягало каждый раз добавлять в конце URL порт `:8080`. Почему бы нам не поставить 80 и забыть про написание  порта в конце URL?)
```
server.port=80
```

### видеть/не видеть hibernate запросы в БД
добавил на всякий случай строки отображение запросов в БД
```
#spring.jpa.show-sql = false
spring.jpa.show-sql = true
```


## Database.properties.

### Всегда добавляйте dialect!
Измените нижепредставленную строчку в `application.properties` на ту, которая вам требуется в зависимости от БД. Я использую MySQL, поэтому у меня будет написано MySQL диалект
```
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL8Dialect
```

### Почему данный файл отдельно от application?
В случае, если вы захотите в следующий раз кому-то передать проект, но забудете убрать из него логин, пароль, URL на БД, как в этот раз,
то злоумышленник сможет получить доступ к вашим данным. Чтобы этого избежать, данные БД можно вынести в отдельную папку и в `.ignore` убрать ее,
но сказать человеку, чтобы он добавил ее у себя в проекте. ИЛИ отправить пустую версию данного файла, а после как-то через gitHub (точно не помню как)
запретить обновлять данный файл.


## User/Users

Да, я сменил `Users` на `User`, потому что один человек прописывается в объект, а не несколько.

### Огромное количество Getter and Setters.
Оу май гад. Представим ситуацию, что у объекта 12 аттрибутов, таких объектов 10. 120 аттрибутов всего и для каждого пишем гет и сет. Итого: 240 Getters and Setters. Это легко убрать с помощью LomBok, которые генерирует Getters And Setters за вас.
Ссылка будет ниже

### Не используйте GenerationStrategy.IDENTITY
Это не самый лучший выбор стратегии... В своем случае я выбрал "кастомную стратегию". Я иммитирую `autoincrement`.


На этом все! Надеюсь помог. 

Послезные ссылки и ресурсы: 
- Baeldung.com
- https://vladmihalcea.com/
- https://ru.stackoverflow.com/q/993198/275232
- https://ru.stackoverflow.com/q/994237/275232




