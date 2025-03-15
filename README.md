### Struktura projektu

Tak teoretycznie ma wyglądać struktura projektu,
ale w praktyce to można to zmieniać bo i tak w praniu wyjdzie,
że coś jest nie tak xd

W folderze frontend znajduję się kod związany z parsowaniem kodu simple.
W folderze pkb znajduję się kod grafów, które mają być wygenerowane na podstawie kodu simple.
Grafy te mają mieć też metody, które będą wykorzystywane do odpowiadania na zapytania pql.
W folderze pql znajduję się kod odpowiedzialny za parsowanie zapytań pql.

### Testy

W folderze tests polecam wrzucać swoje testy do pipetestera. 
Mogą się też przydać komuś innemu xd

### Testowanie pipetesterem

Żeby mieć plik .jar, którego można użyć do pipetestera należy skorzystać z `mvn package`
Wtedy w folderze target pojawi się jar, do którego ścieżkę należy wrzucić do pipetestera
w zakładce "Applications". Po zmianie w kodzie trzeba na nowo generować plik .jar z pomocą
`mvn package`.
