import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.URL;
import java.util.List;
public class LoaderTest {
    @Test
    public void testLoadFromLittleCSV() throws Exception {
        // CSV simulado como string
        String fakeCsv =
                "ID,Nombre,Apellido,Artista,Album,Canción,Duración\n" +
                        "1,Juan,Pérez,BTS,Map of the Soul: 7,Intro: Persona,2:51\n" +
                        "1,Juan,Pérez,BTS,Map of the Soul: 7,Boy With Luv,3:49\n";

        // Creamos un archivo temporal con el contenido
        File tempFile = File.createTempFile("test", ".csv");
        try (FileWriter fw = new FileWriter(tempFile)) {
            fw.write(fakeCsv);
        }

        // Ejecutamos la función
        List<Occupant> occupants = Loader.loadFromCSV(tempFile.getAbsolutePath());

        // Comprobacion de que se añade un ocupante correctamente
        assertEquals(1, occupants.size());
        Occupant occupant = occupants.getFirst();
        assertEquals("Juan Pérez", occupant.getName());
        assertEquals(2, occupant.getSongs().size());

        // Comprobacion de que se añade una canción correctamente
        Song song1 = occupant.getSongs().getFirst();
        assertEquals("Intro: Persona", song1.getTitle());
        assertEquals(171, song1.getDuration());

        // Comprobacion de que se añade un artista correctamente
        Album album = song1.getAlbum();
        assertEquals("Map of the Soul: 7", album.getTitle());
        assertEquals("BTS", album.getArtist().getName());

        // Limpieza
        tempFile.delete();
    }

    @Test
    public void testCargaDesdeArchivoCSVReal() {
        // Obtener la ruta real del archivo en src/test/resources
        URL resource = getClass().getClassLoader().getResource("discos_usuarios.csv");
        assertNotNull(resource, "El archivo CSV no fue encontrado");

        File file = new File(resource.getFile());
        assertTrue(file.exists());

        // Llamar a la función real
        List<Occupant> occupants = Loader.loadFromCSV(file.getAbsolutePath());

        // Comprobar que se carag
        assertEquals(60, occupants.size());

        Occupant occupant = occupants.getFirst();
        assertEquals("Juan Pérez", occupant.getName());
        assertEquals(5, occupant.getSongs().size());

        Song s1 = occupant.getSongs().getFirst();
        assertEquals("Intro: Persona", s1.getTitle());
        assertEquals("Map of the Soul: 7", s1.getAlbum().getTitle());
        assertEquals("BTS", s1.getAlbum().getArtist().getName());
        assertEquals(171, s1.getDuration());
    }

}
