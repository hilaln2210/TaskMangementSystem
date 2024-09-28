# Task Management System - Client/Server

## תוכן עניינים:
1. [מבוא](#מבוא)
2. [דרישות מערכת](#דרישות-מערכת)
3. [ארכיטקטורה של המערכת](#ארכיטקטורה-של-המערכת)
4. [רכיבי המערכת](#רכיבי-המערכת)
   - [Client (צד לקוח)](#client-צד-לקוח)
   - [Server (צד שרת)](#server-צד-שרת)
   - [מודול אלגוריתמים](#מודול-אלגוריתמים)
5. [תכונות המערכת](#תכונות-המערכת)
6. [התקנה והרצה](#התקנה-והרצה)
7. [שימוש בסיסי](#שימוש-בסיסי)
8. [בדיקות](#בדיקות)
9. [תיעוד קוד](#תיעוד-קוד)
10. [מסקנות וייעול עתידי](#מסקנות-ויעול-עתידי)

## מבוא

מערכת ניהול המשימות היא מערכת מבוססת Client/Server המאפשרת למשתמשים לנהל את משימותיהם בצורה נוחה, יעילה ומסודרת. המערכת בנויה בשכבות, כאשר כל רכיב אחראי על פעולה ספציפית ובכך נשמרת חלוקה ברורה של תחומי אחריות ותחזוקה קלה.

### מטרות המערכת:
- לספק ממשק גרפי פשוט לניהול משימות.
- לאפשר שמירה, עריכה ומחיקה של משימות באמצעות תקשורת עם שרת.
- לנהל משימות בצורה גמישה כולל מיון, חיפוש, והוספת תזכורות אוטומטיות.

## דרישות מערכת

- **Java** 17 ומעלה
- **JavaFX** לפיתוח ממשקי משתמש גרפיים
- **Maven** לניהול פרויקטים
- **JUnit** לבדיקות יחידה
- **CalendarFX** לשימוש בלוח שנה
- **GSON** לטיפול באובייקטים JSON

## ארכיטקטורה של המערכת

המערכת בנויה בשכבתיות ומחולקת לשלושה חלקים עיקריים:
1. **Client** - אחראי על ממשק המשתמש והאינטראקציה עם השרת.
2. **Server** - מנהל את הנתונים ומבצע לוגיקה עסקית.
3. **Algorithm Module** - משלב אלגוריתמים שונים למיון ולחיפוש משימות.

### תרשים ארכיטקטורה כללי:
```
 +-------------------+       +-------------------+
 |     Client        |       |     Server        |
 +-------------------+       +-------------------+
 |  TaskController   | <---->|   TaskService     |
 |  TaskModel        |       |   SortingAlgorithm|
 +-------------------+       +-------------------+
        |                            |
        V                            V
 +-------------------+       +-------------------+
 |    GUI (JavaFX)   |       |  Database (File)  |
 +-------------------+       +-------------------+
```

## רכיבי המערכת

### Client (צד לקוח)
הלקוח מספק ממשק גרפי לניהול משימות באמצעות JavaFX. המשתמשים יכולים ליצור משימות חדשות, לערוך משימות קיימות ולמחוק אותן. בנוסף, קיימת יכולת למיין ולחפש משימות בהתאם לקריטריונים שונים.

#### מחלקות עיקריות:
- **TaskController** - אחראי על ניהול הלוגיקה של הלקוח ותיאום בין ממשק המשתמש לשרת.
- **TaskModel** - מייצגת את המודל של משימה בודדת.

### Server (צד שרת)
השרת מטפל בבקשות מהלקוח, שומר את המשימות ומבצע פעולות לוגיות כמו מיון וסינון משימות. השרת מקבל את הנתונים בצורת JSON ומחזיר תגובות מתאימות.

#### מחלקות עיקריות:
- **Server** - אחראי על קבלת בקשות מהלקוח והפעלת התהליכים המתאימים.
- **TaskService** - מממש את הלוגיקה של המערכת כולל מיון ושמירת משימות.

### מודול אלגוריתמים
מודול האלגוריתמים מכיל את הלוגיקה למיון משימות. האלגוריתם הממומש הינו גנרי ומאפשר מיון לפי קריטריונים שונים, כמו תאריך יעד או סדר עדיפות.

#### מחלקות עיקריות:
- **SortingAlgorithm** - מבצע מיון משימות לפי Comparator נתון.
- **SearchAlgorithm** - אחראי על חיפוש משימות בהתאם לקריטריונים שנקבעו מראש.

## תכונות המערכת
- **יצירת משימות** - המשתמש יכול ליצור משימות חדשות עם תאריכי יעד, תיאורים, סדר עדיפויות וסטטוס.
- **עריכת משימות** - המשתמש יכול לערוך משימות קיימות ולשנות את פרטיהן.
- **מחיקת משימות** - מחיקה של משימות בקלות מהמערכת.
- **מיון וחיפוש** - יכולת למיין את המשימות לפי תאריכים, סדר עדיפויות או סטטוס, ולבצע חיפוש פשוט.
- **תזכורות** - הצגת תזכורות לגבי משימות שמתקרב מועד הביצוע שלהן.

## התקנה והרצה

### התקנה:
1. **שכפול מאגר הקוד:**
   ```bash
   git clone https://github.com/yourusername/TaskManagementSystem.git
   cd TaskManagementSystem
   ```

2. **בניית הפרויקט באמצעות Maven:**
   ```bash
   mvn clean package
   ```

3. **הרצת צד שרת:**
   ```bash
   java -cp target/TaskManagementSystem-1.0-SNAPSHOT.jar com.taskmanager.ServerDriver
   ```

4. **הרצת צד לקוח:**
   ```bash
   java -cp target/TaskManagementSystem-1.0-SNAPSHOT.jar com.taskmanager.ClientMain
   ```

## שימוש בסיסי

### ממשק הלקוח:
- **יצירת משימה חדשה**: מלא את השדות הנדרשים (כותרת, תיאור, תאריך התחלה וסיום, זמן התחלה וסיום, עדיפות) ולחץ על "הוסף".
- **מיון משימות**: בחר את הקריטריון הרצוי למיון מתוך התפריט.
- **עריכת משימה**: בחר משימה ברשימה ולחץ על "ערוך".
- **מחיקת משימה**: בחר משימה ברשימה ולחץ על "מחק".
- **תצוגת לוח שנה**: התזכורות יופיעו בלוח השנה לפי תאריכי היעד.

### ממשק השרת:
- כל הבקשות מהלקוח מתבצעות דרך HTTP באמצעות JSON. השרת מאזין לבקשות מהלקוח ומטפל בהתאם.

## בדיקות

### בדיקות יחידה:
הפרויקט כולל בדיקות יחידה באמצעות JUnit לכל אחד מהרכיבים המרכזיים במערכת. לדוגמה:
- **AlgoTaskManagerTest** - בודק את הפונקציונליות של אלגוריתמי המיון והחיפוש.
- **TestTaskService** - בודק את תקינות הלוגיקה של מחלקת השירות.

### הרצת הבדיקות:
```bash
mvn test
```


## מסקנות וייעול עתידי

### ייעול עתידי:
- **אינטגרציה עם בסיס נתונים**: ניתן להחליף את קבצי הטקסט בבסיס נתונים לניהול משימות מורכב יותר.
- **ממשק Web**: פיתוח ממשק מבוסס Web יאפשר למשתמשים לנהל את המשימות שלהם מכל מקום.
- **שיפור ביצועים**: ניתן לשלב מנגנונים מתקדמים יותר של ניהול זיכרון לשיפור מהירות העבודה במערכות גדולות.

---

