# FileHelper
This is an application that helps to manage image files using the description and keywords from EXIF. FileHelper scans the folder with pairs of images (jpg + eps), which differ only in extensions. And selects those jpg-files that have keywords in the EXIF. Then the FileHelper moves the jpg-files and their eps-pairs to the specified folders. The number of target folders is unlimited.

### Acknowledgments

I use <a href="https://drewnoakes.com/code/exif/" target="_blank">metadata-extractor</a>, here <a href="https://github.com/drewnoakes/metadata-extractor" target="_blank">the repository</a> on Github, for EXIF reading. Thank you, guys!