
#!/bin/bash

i="0"

while [ $i -lt 10000 ]
do
sbt test 
i=$[$i+1]
done
