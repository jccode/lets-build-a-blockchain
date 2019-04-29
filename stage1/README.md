
Usage
======

* Launch server

       sbt stage1/run
       
       
* Launch client console

       sbt stage1/console
       :load stage1/app/Client.scala
       val c = Client()
       c.get_balance("michael")
       c.create_user("sara")
       c.transfer("michael", "sara", 5000)
