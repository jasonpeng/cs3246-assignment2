cs3246-assignment2
==================

Image analysis project

==================

Set Up:

1. Create a folder called "uploaded". The front end will upload the new query photo to this folder: /uploaded/
2. The database images should be stored in: /image/
3. Generate index file by running the main function in GenerateImageIndex Class, with -Xmx4g as VirtualMachine parameter. The indexed file is named colorsimilarity.txt and will be stored in: /

Run: 

1. The user uploads a new query image. It's going to be uploaded to /uploaded/
2. The front end call the public static method: query(String fileName) in the Class WebServiceHandler.
3. The relevent document names will be printed out using System.out.println().
4. The front end will notice the document names and display the images.
