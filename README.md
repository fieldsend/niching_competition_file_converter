# niching_competition_file_converter


The file format defined for entries to the GECCO 2019 Competition on Niching Methods for Multimodal Optimization

http://www.epitropakis.co.uk/gecco2019/#submission

Gives two accptable ways of generating the .dat files which tracks estimated modes discovered by and algorithm. These have one of three action flags on each line, as taken from the link above:

-- Reset archive and add solution (0): This action deletes everything that has been reported in the current file and adds the current solution as the first solution in the archive.
-- Add solution (1): This is the normal case where the reported solution is added in the archive.
-- Delete solution (-1): In this case, we search through the archive and delete the reported solution. If the archive does not contain this solution, we do not take any action. Notice that the solutions are hashed by the following features: Solution (x1,x2,...,xd), fes, and time. As such, a solution will be removed only if the exact same solution is found within the archive.

As such one may write the file so that each time the mode set changes the set is written out with the first solution's action being 0, and all following with action 1. This ensures the dat file represents completely the active mode set on each update, with minium changes to the underlying algorithm. 

Unfortunately the result files can grow rather large (I ended up with a zip file of results over 1GB). This tool converts files constructed in the first method to that using the diff approach (using a 0 flag at the start, and all subseqent lines having either 1 or -1 actions). 

This is acheived by processing each set block in turn (i.e. block starting with a 0 and then zero to many lines with 1 actions, until the next rest is reached), this is then compared to the previous copied block, and any solutions no longer present are written out with a -1 action, and any new with a 1 action. 

Making a jar file from the package with LoopConvert as the Main class will enable you to perform 

```
>> java -jar converter.jar
```

will convert all files in the current directory, named as required in the competition, from problem 1 to 20 and run 1 to 50, into the more parsimonious represntation and save them in a folder called reformatted.
