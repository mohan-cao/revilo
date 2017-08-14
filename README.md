<div align="center">
<a href="https://github.com/mohan-cao/revilo"><img style="display:inline-block;" src="./revilo.png" alt="Our logo WIP"></a>
<br>
<sup>Master branch is currently </sup><a href="https://travis-ci.com/mohan-cao/revilo"><img style="display:inline-block;" src="https://travis-ci.com/mohan-cao/revilo.svg?token=geujzTyWrzPD96doTGqK&branch=master" alt="Build status"></a>
</div>

# Revilo Task Scheduler

The official page for the Revilo (**ruh -*VEE*- low**) Task Scheduler Project!

Planning and documentation can be found here: [Revilo Wiki](https://github.com/mohan-cao/revilo/wiki)

Shared Google Drive folder for resources: [Revilo Project Folder](https://drive.google.com/open?id=0B6EjuC_mOLoyZ2NCa0RuOEp4Y3c)

For developers, please only merge branches if they pass the unit tests on the build server!

## The Team
| Name         | UPI     | Uni ID | GitHub    |
| ------------ | ------- | ------ | --------- |
| Abby Shen    | [ashe848](mailto:ashe848@aucklanduni.ac.nz) | 269481021 | [ashe848](http://www.github.com/ashe848)   |
| Aimee Tagle  | [atag549](mailto:atag549@aucklanduni.ac.nz) | 8194348 | [abtagle](http://www.github.com/abtagle)   |
| Mohan Cao    | [mcao024](mailto:mcao024@aucklanduni.ac.nz) | | [mohan-cao](http://www.github.com/mohan-cao) |
| Michael Kemp | [mkem114](mailto:mkem114@aucklanduni.ac.nz) | 6273632 | [mkem114](http://www.github.com/mkem114)   |
| Terran Kroft | [tkro003](mailto:tkro003@aucklanduni.ac.nz) | 6869815 | [itemic](http://www.github.com/itemic)    |

## Quick Startup Guide

Clone the project anywhere

`git clone git@github.com:mohan-cao/revilo.git`

Using IntelliJ IDEA:

1. Import project as Maven project
2. Wait for IntelliJ to fetch the required resources
3. Yay!

Using Eclipse (Start from Eclipse instead of importing clone!):

1. Right click the left-hand side package explorer (where you have projects and stuff)
2. Go to Import... > Git > Clone from repository
3. If ssh not set up: clone with HTTPS `https://github.com/mohan-cao/revilo.git` 
4. Set up as new project using Project Wizard
5. Name it "revilo"
6. Right click project > Configure > Convert into Maven project
7. Done! 

## Installation and Running Unit Tests

`mvn clean` - Cleans project (start from scratch)

`mvn clean test` - Run unit test (from scratch)

`mvn test` - Run unit test

`mvn package` - Package classes into a jar. JAR is executable.

`mvn install` - Do all the above and install the package
