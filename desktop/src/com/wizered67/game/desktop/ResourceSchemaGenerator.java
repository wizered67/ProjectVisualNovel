package com.wizered67.game.desktop;


import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.XmlIO.MixedXmlReader;

import static com.badlogic.gdx.utils.XmlReader.Element;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads Resources.xml file and uses it to generate a schema containing
 * enumerations for resources of each type.
 * @author Adam Victor
 */
public class ResourceSchemaGenerator {
    private static final String ANIMATIONS_DIRECTORY = "Animations";
    private static final String BACKGROUNDS_DIRECTORY = "Backgrounds";
    private static final String MUSIC_DIRECTORY = "Music";
    private static final String SOUNDS_DIRECTORY = "Sounds";

    private static final String ANIMATIONS_TAG = "animation_files";
    private static final String BACKGROUNDS_TAG = "backgrounds";
    private static final String MUSIC_TAG = "music";
    private static final String SOUNDS_TAG = "sounds";
    private static final String CHARACTERS_TAG = "characters";

    private static final String ANIMATION_ATLAS_TYPE = "animationAtlasResource";
    private static final String ANIMATIONS_TYPE = "animationResource";
    private static final String BACKGROUNDS_TYPE = "backgroundResource";
    private static final String MUSIC_TYPE = "musicResource";
    private static final String SOUNDS_TYPE = "soundResource";
    private static final String CHARACTER_TYPE = "characterType";
    private static final String RESOURCE_TYPE = "resourceType";

    private static final String SIMPLE_TYPE = "xs:simpleType";
    private static final String RESTRICTION = "xs:restriction";
    private static final String STRING = "xs:string";
    private static final String ENUMERATION = "xs:enumeration";
    private static final String UNION = "xs:union";
    private static final String ANY_TYPE = "anyType";

    private static final Pattern RESOURCE_PATTERN = Pattern.compile("\\s*!?(.+)\\s+(.+)\\s*");
    private static XmlWriter xmlWriter;

    private static Set<String> identifiers = new HashSet<String>();
    private static boolean strict;

    public static void main(String[] args) {
        try {
            String destination = args[0];
            InputStream xmlFile = new FileInputStream("Resources.xml");
            MixedXmlReader xmlReader = new MixedXmlReader();
            FileWriter writer = new FileWriter(destination, false);
            xmlWriter = new XmlWriter(writer);
            Element root = xmlReader.parse(xmlFile);
            strict = root.getBooleanAttribute("strict", false);
            writeHeader();
            writeAny();
            writeAnimations(root);
            writeBackgrounds(root);
            writeMusic(root);
            writeSounds(root);
            writeCharacters(root);
            writeResources();
            writeEnd();

            xmlWriter.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void writeHeader() {
        try {
            xmlWriter.element("xs:schema")
                    .attribute("attributeFormDefault", "unqualified")
                    .attribute("elementFormDefault", "qualified")
                    .attribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
            System.out.println("Wrote header.");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void writeAny() {
        try {
            simpleType(ANY_TYPE);
                restriction(STRING);
                xmlWriter.pop();
            xmlWriter.pop();
            System.out.println("Wrote 'any' type.");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void writeAnimations(Element root) {
        Map<String, String> resources = getResources(root, ANIMATIONS_TAG);
        writeIdentifiers(ANIMATION_ATLAS_TYPE, resources.keySet());
        verifyResources(resources, ANIMATIONS_DIRECTORY);
        System.out.println("Wrote animation atlases.");
        writeAnimationNames(resources);
    }

    private static void writeAnimationNames(Map<String, String> resources) {
        Set<String> animationNames = new HashSet<String>();
        for (String identifier : resources.keySet()) {
            String filename = resources.get(identifier);
            String extension = "";
            int i = filename.lastIndexOf('.');
            if (i > 0) {
                extension = filename.substring(i + 1);
            }
            if (extension.equals("pack")) {
                File file = new File("Animations/" + filename);
                try {
                    Scanner scanner = new Scanner(file);
                    scanner.next(); //skip over filename
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        if (line.indexOf(':') < 0 && !line.isEmpty()) { //no colon so it should be the name of an animation
                            animationNames.add(identifier + "_" + line);
                        }
                    }
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
        writeIdentifiers(ANIMATIONS_TYPE, animationNames);
    }

    private static void writeBackgrounds(Element root) {
        Map<String, String> resources = getResources(root, BACKGROUNDS_TAG);
        writeIdentifiers(BACKGROUNDS_TYPE, resources.keySet());
        verifyResources(resources, BACKGROUNDS_DIRECTORY);
        System.out.println("Wrote backgrounds.");
    }

    private static void writeMusic(Element root) {
        Map<String, String> resources = getResources(root, MUSIC_TAG);
        writeIdentifiers(MUSIC_TYPE, resources.keySet());
        verifyResources(resources, MUSIC_DIRECTORY);
        System.out.println("Wrote music.");
    }

    private static void writeSounds(Element root) {
        Map<String, String> resources = getResources(root, SOUNDS_TAG);
        writeIdentifiers(SOUNDS_TYPE, resources.keySet());
        verifyResources(resources, SOUNDS_DIRECTORY);
        System.out.println("Wrote sounds.");
    }

    private static void writeCharacters(Element root) {
        Element tags = root.getChildByName(CHARACTERS_TAG);
        Set<String> identifiers = new HashSet<String>();
        for (int c = 0; c < tags.getChildCount(); c += 1) {
            Element character = tags.getChild(c);
            if (!character.getName().equals("character")) {
                System.err.println("Invalid element in 'characters' group.");
            }
            String id = character.getAttribute("id");
            if (identifiers.contains(id)) {
                identifierError(id);
            }
            identifiers.add(id);
        }
        writeIdentifiers(CHARACTER_TYPE, identifiers);
        System.out.println("Wrote characters.");
    }

    private static void writeResources() {
        writeIdentifiers(RESOURCE_TYPE, identifiers);
        System.out.println("Wrote resources.");
    }

    private static void writeEnd() {
        try {
            xmlWriter.pop();
            System.out.println("Finished writing schema.");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static Map<String, String> getResources(Element root, String type) {
        Map<String, String> resources = new HashMap<String, String>();
        Element files = root.getChildByName(type);
        for (int i = 0; i < files.getChildCount(); i += 1) {
            Element child = files.getChild(i);
            if (child.getName().equals("text")) {
                String text = child.getText();
                text = text.replaceAll("\\r", "");
                String[] lines = text.split("\\n");
                for (String line : lines) {
                    line = line.trim();
                    Matcher matcher = RESOURCE_PATTERN.matcher(line);
                    String identifier, filename;
                    if (matcher.matches()) {
                        identifier = matcher.group(1);
                        filename = matcher.group(2);
                    } else {
                        if (line.startsWith("!")) {
                            line = line.substring(1);
                        }
                        identifier = line;
                        filename = line;
                    }
                    if (resources.containsKey(identifier)) {
                        identifierError(identifier);
                    }
                    resources.put(identifier, filename);
                }
            }
        }
        return resources;
    }

    private static void writeIdentifiers(String type, Set<String> identifiers) {
        try {
            simpleType(type);
                if (!strict) {
                union(ANY_TYPE);
                    simpleType(); }
                        restriction(STRING);
                            for (String identifier : identifiers) {
                                enumeration(identifier);
                            }
                        xmlWriter.pop();
                if (!strict) {
                    xmlWriter.pop();
                xmlWriter.pop(); }
            xmlWriter.pop();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void verifyResources(Map<String, String> resources, String directory) {
        for (String identifier : resources.keySet()) {
            if (identifiers.contains(identifier)) {
                identifierError(identifier);
            }
            identifiers.add(identifier);
            String filename = resources.get(identifier);
            File file = new File(directory + "/" + filename);
            if (!file.exists()) {
                System.err.println("Missing resource '" + directory + "/" + filename + "'.");
            }
        }
    }

    private static void identifierError(String id) {
        System.err.println("Identifier '" + id + "' is already in use.");
    }

    private static void simpleType(String name) {
        try {
            xmlWriter.element(SIMPLE_TYPE)
                .attribute("name", name);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void simpleType() {
        try {
            xmlWriter.element(SIMPLE_TYPE);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void restriction(String base) {
        try {
            xmlWriter.element(RESTRICTION)
                    .attribute("base", base);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void enumeration(String value) {
        try {
            xmlWriter.element(ENUMERATION)
                    .attribute("value", value)
                    .pop();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void union(String type1, String type2) {
        try {
            xmlWriter.element(UNION)
                    .attribute("memberTypes", type1 + " " + type2)
                    .pop();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void union(String type) {
        try {
            xmlWriter.element(UNION)
                    .attribute("memberTypes", type);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
