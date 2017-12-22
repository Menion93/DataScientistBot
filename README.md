# Data Scientist Bot

This bot was built for my master thesis at the "Università degli Studi Roma 3". It implements an interactive chabot that helps you build a Data Science pipeline.

## Installing

You need to install first Mongodb and Postrges servers. You also require at least 2GB of ram and 4GB of free space in your hard disk.

### Steps

* Download from this [link](https://drive.google.com/open?id=1AppPEWy0277HyNcUNZq44W-PqeVaaxIM) AcsdbBackup.backup 
* Create a database named Acsdb on postgresql
* Import the backup "AcsdbBackup.backup" into Acsdb database you have just created 
* Extend you heap memory at execution time adding the parameter "-Xmx2048m" to your vm parameters (in Run Configuration on Eclipse or IntelliJ)

## Run the chatbot
After you have started the mongodb and the postgres services, just run the class "/src/main/java/core/DataScientistBot.java" and start using the bot

# References
* [WebTables: Exploring the Power of Tables on the Web](https://homes.cs.washington.edu/~alon/files/vldb08webtables.pdf) - For the theory behind the Acsdb Database
* [Learning Feature Engineering for Classification](https://www.ijcai.org/proceedings/2017/0352.pdf) - For the realization of the LFE tool

## Author

* **Andrea Salvoni** - *Initial work* - [Menion93](https://github.com/Menion93)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

