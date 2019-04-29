
Usage
=====

Launch server
-------------

* Launch server 1 at 9000

      sbt -Dhttp.port=9000 -DpeerPort=9001 stage2/run 

* Launch server 1 at 9001

      sbt -Dhttp.port=9001 -DpeerPort=9000 stage2/run
      