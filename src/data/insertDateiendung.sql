insert into dateiendung (datatype,extension) values ('C:\Users\Isabella\Documents\Schule\3 HTL\3\ITP\Projekt\dataorganizer\Bilder','jpg,jpeg,gif,png');
insert into dateiendung (datatype,extension) values ('C:\Users\Isabella\Documents\Schule\3 HTL\3\ITP\Projekt\dataorganizer\Dokumente','pdf,docx,xlxs,zip');
insert into dateiendung (datatype,extension) values ('C:\Users\Isabella\Documents\Schule\3 HTL\3\ITP\Projekt\dataorganizer\Video','mp4,avi,wmv');
insert into dateiendung (datatype,extension) values ('C:\Users\Isabella\Documents\Schule\3 HTL\3\ITP\Projekt\dataorganizer\Audio','mp3,wma,ogg,flac');

delete  from regexrules;
insert into regexrules(ordner,regex) values ('C:\Users\Isabella\Documents\Schule\3 HTL\3\ITP\Projekt\dataorganizer\Bilder','guitar.sql');