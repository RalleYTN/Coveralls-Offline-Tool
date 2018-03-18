[![Build Result](https://api.travis-ci.org/RalleYTN/Coveralls-Offline-Tool.svg?branch=master)](https://travis-ci.org/RalleYTN/Coveralls-Offline-Tool)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/918e3d29d66343a88df68a0139668737)](https://www.codacy.com/app/ralph.niemitz/Coveralls-Offline-Tool?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=RalleYTN/Coveralls-Offline-Tool&amp;utm_campaign=Badge_Grade)

# Description

This is a tool that I created out of need.
Some of my libraries cannot be tested in the headless linux environment of Travis CI.
Because of that I cannot send a test report to Coveralls after building.
Luckily both Travis CI and Coveralls have APIs.
What this tool does is retrieve the latest job id of the latest build for a specific repository from Travis CI and use it to send a coverage report which
was created by Eclipse on your local machine to Coveralls.

## Install

1. [Download](https://github.com/RalleYTN/Coveralls-Offline-Tool/releases) the latest version of the tool
1. Unzip the JAR and `report.dtd` files in a new directory.
1. Done

## Usage

### Create a coverage report with Eclipse

1. Run the JUnit Tests with this button ![Step 1](https://raw.githubusercontent.com/RalleYTN/Coveralls-Offline-Tool/master/img/step1.png)
1. Make a right click in the coverage view and click on export session ![Step 2](https://raw.githubusercontent.com/RalleYTN/Coveralls-Offline-Tool/master/img/step2.png)
1. Export the session in XML format and name it `report.xml`. The name is important! ![Step 3](https://raw.githubusercontent.com/RalleYTN/Coveralls-Offline-Tool/master/img/step3.png)
1. Copy the `report.xml` in the directory with the JAR of this tool.

### Run the tool

1. Open up your console
1. run `java -jar <location of cofftool-X.X.X.jar> <location of the source code on default package level (make sure that unit tests are not in the same directory)> <your Travis CI access token> <the repository (YourName/YourRepoName)>`
1. The program will tell you what to do from there on

## Changelog

### Version 1.1.0

- Fixed `pom.xml`
- Much more output now
- Now you get asked if you want to submit the coverage report

### Version 1.0.0

- Release

## License

```
MIT License

Copyright (c) 2018 Ralph Niemitz

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```