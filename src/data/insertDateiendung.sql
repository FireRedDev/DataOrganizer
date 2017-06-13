insert into dateiendung (datatype,extension) values ('Bilder','jpg,jpeg,gif,png');
insert into dateiendung (datatype,extension) values ('Dokumente','pdf,docx,xlxs,zip');
insert into dateiendung (datatype,extension) values ('Video','mp4,avi,wmv');
insert into dateiendung (datatype,extension) values ('Audio','mp3,wma,ogg,flac');

delete  from regexrules;
insert into regexrules(ordner,regex) values ('gui','gui');
insert into regexrules(ordner,regex) values ('imag','imag');