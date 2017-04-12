# CountMeUp

This is the domain for an application that will monitor elections, and provide nearish real time results.

The project was built in IntelliJ using scala, and has 2 branches. The master branch uses HashMaps to keep and store its data, and the mongoImpl branch uses a mongo DB.

# Setup
Clone the repo, and import the CountMeUp folder into your IDE as an SBT project using java 1.8

If you want to test the mongo branch you will also have to set up a mongo instance on your local machine, and run the following commands
* `use bbc`
* `db.createCollection(votes)`
* `db.createCollection(users)`
* `db.createCollection(elections)`
* `db.createCollection(candidates)`

To package, run `sbt clean compile test package`

# Usage
This will compile into a jar that exposes 3 domain methods, one for user, elections, and candidates.
* The candidate domain allows you to create and read candidates.
* The election domain will add elections, and get election results
* The user domain creates and retrieves users, along with allows a user to vote in an election.

# Final Update
The best way to interact with this domain is from the tests. The project will compile down to a jar that can be imported into other projects for use. But I spent all my time chasing down performance numbers and simply ran out of time to build any such application.

I wanted to build a solution that could be reused, and could keep track of multiple elections without having to create new users for each. Also chose to keep track of each individual vote, as opposed to incrementing tallys. While simply incrementing a number each time a user voted would be significantly quicker, It would not have given me an platform to show off more impressive code. I had reached out early on in this assignment to get a better scope of the project when I was designing it, and was effectively told I was free to interpret the requirements as I liked. So I chose the hard route to better show what I can do.

I chose to go with a hash map as my original data storage to reduce the amount of setup anyone running this code (including myself) would have to go through. After deciding I wanted to try an database implantation, I chose Mongo because its setup was very quick, and I wanted more experience using a NoSql DB. If this were to go to production, I would have had liked to have had a conversation with a product owner to get a better idea of the scope, and may have picked a different database with that information.

The best performance numbers I was able to get for retrieving an election with 10 million votes was just under 3.5 seconds, using the hash map data store. I'm sure this number will go down with mongo, but I was not able to populate that much data, I crashed my computer and ran into a number of OutOfMemmoryErrors attempting to do so.

At the end of the day, I can only hope that I have been able to clearly demonstrate my ability to write code and work in a professional setting. If you have any questions about why I did what I did, I did try to condense my thoughts into the nightly update files. But if there is anything more, please do not hesitate to ask me directly. 

Regardless of your decision to invite me to the assessment centre, its been a hell of a lot of fun working on this project, 

Thank you. 
