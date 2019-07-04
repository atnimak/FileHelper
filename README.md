# FileHelper
This is an application that helps to manage image files using the description and keywords from EXIF. FileHelper scans the folder with pairs of images (jpg + eps), 
which differ only in extensions. And selects those jpg-files that have keywords in the EXIF. Then the 
FileHelper moves the jpg-files and their eps-pairs to the specified folders. The number of target folders is unlimited.

### Acknowledgments

I use <a href="https://drewnoakes.com/code/exif/" target="_blank">metadata-extractor</a>, 
here <a href="https://github.com/drewnoakes/metadata-extractor" target="_blank">the repository</a> on Github, for EXIF reading. Thank you, guys!

### Use
The program can be run with or without arguments. When you run the program without arguments, 
the program itself will ask you questions about the directory with the source files, about the directories where to copy the file. 
Also, the program will ask about whether to delete files from the source directory.
There are two startup modes with arguments. The first way:
```
C:\sourсeDir C:\targetDir1 C:\targetDir2 n y n
```
The second startup mode with arguments: 
use the "-c" argument. In this case, the program will take all the settings and directories from a file "configurationFile.txt",
that should be located in the same directory as the executable file FileHelper.
The "configurationFile.txt" file is organized as follows.
Where the first argument is the source directory. The second and subsequent arguments target directories.
Then the argument "n", indicating that the listing of target directories is complete.
Then the argument is "n" or "y", indicating the need to delete files from the source directory. 
If you want to delete files use "y", if you don`t want to delete files use "n".
Then the argument "n" or "y" indicates that the input of the arguments is complete.

Then the argument "n" or "y", which indicates whether or not the input of the arguments is completed. 
This argument is the answer to the question "Would you like to add another task?" 
If you want to add another task: use the argument "y", and then enter the source directory, target directories, "n", "y" ,
and again answer the question whether you want to add another task. Here is an example of configuration file:
```
C:\sourseDir
C:\targetDir1
C:\targetDir2
C:\targetDir3
C:\targetDir4
n
y
y
C:\sourseDir
C:\targetDir1
C:\targetDir2
C:\targetDir3
C:\targetDir4
n
y
y
C:\sourseDir
C:\targetDir1
C:\targetDir2
C:\targetDir3
C:\targetDir4
n
y
n
```