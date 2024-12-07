# How to Use JSON.simple in Your Java Project

This guide will help you set up **JSON.simple** in **VS Code**, **IntelliJ IDEA**, and **Eclipse**.

## 1. Download JSON.simple

1. Go to the Maven repository page for JSON.simple: [JSON.simple Maven](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple).
2. Download the latest `.jar` file from the **Files** section.

---

## 2. Add JSON.simple to Your Project

### 2.1: In Visual Studio Code (VS Code)

1. **Create a `lib` Folder**:
   - In your project directory, create a folder named `lib`.
   - Place the downloaded `json-simple-x.x.x.jar` file in this `lib` folder.

2. **Modify `javac` and `java` Commands** (if running manually):
   - When compiling and running Java files, include the `lib` folder:
     ```bash
     javac -cp lib/json-simple-x.x.x.jar Main.java
     java -cp .:lib/json-simple-x.x.x.jar Main
     ```

3. **For Build Tools (Gradle/Maven)**:
   - If you're using Maven or Gradle, add JSON.simple as a dependency in your `pom.xml` or `build.gradle`.

---

### 2.2: In IntelliJ IDEA

1. **Add the Library to Your Project**:
   - Right-click on your project folder and select **Open Module Settings**.
   - Go to the **Libraries** tab and click the `+` icon.
   - Select **Java**, then navigate to the downloaded `json-simple-x.x.x.jar` file and add it.

2. **Verify**:
   - Check the `External Libraries` section in the Project tool window. You should see JSON.simple listed.

---

### 2.3: In Eclipse

1. **Add the Library**:
   - Right-click on your project in the **Package Explorer**.
   - Select **Build Path** > **Configure Build Path**.
   - Go to the **Libraries** tab and click **Add External JARs**.
   - Browse to the location of the `json-simple-x.x.x.jar` file and add it.

2. **Verify**:
   - Expand the project in the Package Explorer and look for `Referenced Libraries`. JSON.simple should be listed.

---

## 3. Example Usage

Here’s a basic example to ensure **JSON.simple** is working:

#### Main.java:
```java
import org.json.simple.JSONObject;

public class Main {
    public static void main(String[] args) {
        JSONObject obj = new JSONObject();
        obj.put("name", "John");
        obj.put("age", 30);

        System.out.println(obj.toJSONString());
    }
}
