# saucedemo.site

Camelot website test

The automated tests were developed using Selenium WebDriver and Java as the primary tools. For the Integrated Development Environment (IDE), Intellij was utilized.

Regarding the testing framework, JUnit in conjunction with Gradle was employed. The intention was to maintain a simplistic structure, with all tests being placed under the "test" folder, and the corresponding objects organized under the "page.objects" folder. The main objective behind using page objects was to segregate the element items from the actual test logic.

Specifically, the test named "LoginParametarized" represents four distinct test cases executed with varying user names. JUnit 5's "ParameterizedTest" library facilitated the execution of these tests with multiple inputs.
