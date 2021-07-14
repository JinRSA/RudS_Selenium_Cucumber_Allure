package stepDefs;

import io.cucumber.java.ParameterType;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import stepDefs.Category;
import stepDefs.SortBy;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class StepDefs {
    private WebDriver m_driver;
    private String m_object, m_city;

    @Step("Открыть Авито")
    @Пусть("открыт ресурс авито")
    public void открытРесурсАвито() {
        try (InputStream input = new FileInputStream("src/main/resources/project.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            String path2ChromeDriver = properties.getProperty("path2ChromeDriver");
            System.setProperty("webdriver.chrome.driver", path2ChromeDriver);
        } catch (Exception ignored) {}

        m_driver = new ChromeDriver();
        m_driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        m_driver.get("https://avito.ru");
        m_driver.manage().window().maximize();

        Screenshot screen = new AShot().takeScreenshot(m_driver);
        saveScreenshot(screen);
    }

    @ParameterType(".*")
    public Category category(String categ) {
        categ = categ.substring(0, 1).toUpperCase() + categ.substring(1).toLowerCase();
        categ = categ.replaceAll("\\p{Punct}", "");
        categ = categ.replaceAll(" ", "_");
        return Category.valueOf(categ);
    }

    @Step("Выбрать категорию {categ}")
    @И("в выпадающем списке категорий выбрана {category}")
    public void вВыпадающемСпискеКатегорийВыбранаОргтехникаИРасходники(Category categ) {
        By by = By.id("category");
        Select categoryDropDownList = new Select(m_driver.findElement(by));
        categoryDropDownList.selectByVisibleText(categ.getValue());

        Screenshot screen = new AShot().takeScreenshot(m_driver);
        saveScreenshot(screen);
    }

    @Step("В поле поиска ввести значение {value}")
    @И("в поле поиска введено значение {string}")
    public void вПолеПоискаВведеноЗначение(String value) {
        m_object = value;
        By by = By.xpath("//input[@data-marker=\"search-form/suggest\"]");
        WebElement search = m_driver.findElement(by);
        search.sendKeys(value);

        Screenshot screen = new AShot().takeScreenshot(m_driver);
        saveScreenshot(screen);
    }

    @Step("Кликнуть по выпадающему списку региона")
    @Тогда("кликнуть по выпадающему списку региона")
    public void кликнутьПоВыпадающемуСпискуРегиона() {
        By by = By.xpath("//div[@data-marker=\"search-form/region\"]");
        m_driver.findElement(by).click();

        Screenshot screen = new AShot().takeScreenshot(m_driver);
        saveScreenshot(screen);
    }

    @Step("В поле регион ввести значение {cityStr}")
    @Тогда("в поле регион введено значение {string}")
    public void вПолеРегионВведеноЗначение(String cityStr) {
//        m_city = cityStr;
        By by = By.xpath("//input[@data-marker=\"popup-location/region/input\"]");
        WebElement searchCity = m_driver.findElement(by);
        searchCity.sendKeys(cityStr);
        by = By.xpath("//strong[text()=\"" + cityStr + "\"]");
        WebElement city = m_driver.findElement(by);
        by = By.xpath("//span[strong[text()=\""+ cityStr + "\"]]");
        m_city = city.findElement(by).getText();
        city.click();

        Screenshot screen = new AShot().takeScreenshot(m_driver);
        saveScreenshot(screen);
    }

    @Step("Нажать кнопку показать объявления")
    @И("нажата кнопка показать объявления")
    public void нажатаКнопкаПоказатьОбъявления() {
        By by = By.xpath("//button[@data-marker=\"popup-location/save-button\"]");
        m_driver.findElement(by).click();

        Screenshot screen = new AShot().takeScreenshot(m_driver);
        saveScreenshot(screen);
    }

    @Step("Открылась страница результатов по запросу {value}")
    @Тогда("открылась страница результаты по запросу {string}")
    public void открыласьСтраницаРезультатыПоЗапросу(String value) {
        By by = By.xpath("//h1[@data-marker=\"page-title/text\"]");
        String result = m_driver.findElement(by).getText();
        assert result.contains(value) : "Страница с результатами по запросу \"" + value + "\" не найдена.";

        Screenshot screen = new AShot().takeScreenshot(m_driver);
        saveScreenshot(screen);
    }

    @Step("Активировать checkbox только с фотографией")
    @И("активирован чекбокс только с фотографией")
    public void активированЧекбоксТолькоСФотографией() {
        By by = By.xpath("//input[@data-marker=\"search-form/with-images\"]");
        WebElement checkBox = m_driver.findElement(by);
        if (!checkBox.isSelected())
            checkBox.sendKeys(Keys.SPACE);
        by = By.xpath("//button[@data-marker=\"search-filters/submit-button\"]");
        m_driver.findElement(by).click();

        Screenshot screen = new AShot().takeScreenshot(m_driver);
        saveScreenshot(screen);
    }

    @ParameterType(".*")
    public SortBy sortBy(String sortBy) {
        sortBy = sortBy.substring(0, 1).toUpperCase() + sortBy.substring(1).toLowerCase();
        sortBy = sortBy.replaceAll("\\p{Punct}", "");
        sortBy = sortBy.replaceAll(" ", "_");
        return SortBy.valueOf(sortBy);
    }

    @Step("В выпадающем списке сортировка выбрано значение {sortBy}")
    @И("в выпадающем списке сортировка выбрано значение {sortBy}")
    public void вВыпадающемСпискеСортировкаВыбраноЗначениеДороже(SortBy sortBy) {
        By by = By.xpath("//select[option[./text()=\"По умолчанию\"]]");
        Select filterByDropDownList = new Select(m_driver.findElement(by));
        filterByDropDownList.selectByVisibleText(sortBy.getBy());

        Screenshot screen = new AShot().takeScreenshot(m_driver);
        saveScreenshot(screen);
    }

    @Step("Вывести в консоль значение названия и цены {count} первых товаров")
    @И("в консоль выведено значение названия и цены {int} первых товаров")
    public void вКонсольВыведеноЗначениеНазванияИЦеныПервыхТоваров(int count) {
        By by = By.xpath("//h3[@itemprop=\"name\"]");
        List<WebElement> resultsName = m_driver.findElements(by);
        by = By.xpath("//span[@data-marker=\"item-price\"]");

        Screenshot screen = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(96)).takeScreenshot(m_driver);
        saveScreenshot(screen);

        List<WebElement> resultsPrice = m_driver.findElements(by);
        assert count <= resultsName.size() : count + ">" + resultsName.size() + "! Значение должно быть в пределах количества выводимых на странице результатов";
        String output = m_city + "; " + m_object + '.';
        for (int i = 0; i < count; ++i)
            output += "\nНаименование: " + resultsName.get(i).getText() + "\nЦена: " + resultsPrice.get(i).getText();
        System.out.println(output);
        m_driver.quit();

        saveStringResult(output);
    }

    @Attachment(value = "Скриншот", type = "image/png")
    public byte[] saveScreenshot(Screenshot screenshot) {
//        ((TakesScreenshot)m_driver).getScreenshotAs(OutputType.BYTES);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(screenshot.getImage(), "PNG", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    @Attachment(value = "Результат")
    public String saveStringResult(String in) { return in; }
}