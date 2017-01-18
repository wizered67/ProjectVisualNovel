package com.wizered67.game.GUI.Conversations.XmlIO;


import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import static com.badlogic.gdx.utils.XmlReader.Element;
import java.io.*;

/**
 * Created by Adam on 1/16/2017.
 */
public class ResourceSchemaGenerator {
    private static final String ANIMATIONS_NAME = "animations";
    private static final String BACKGROUNDS_NAME = "backgrounds";
    private static final String MUSIC_NAME = "music";
    private static final String SOUND_NAME = "sounds";
    private static final String SIMPLE_TYPE = "xs:simpleType";
    private static final String RESTRICTION = "xs:restriction";
    private static final String STRING = "xs:string";
    private static final String ENUMERATION = "xs:enumeration";
    private static final String UNION = "xs:union";
    private static final String ANY_TYPE = "anyType";
    private static XmlWriter xmlWriter;
    public static void main(String[] args) {
        try {
            String destination = args[0];
            InputStream xmlFile = new FileInputStream("Resources.xml");
            MixedXmlReader xmlReader = new MixedXmlReader();
            FileWriter writer = new FileWriter(destination, false);
            xmlWriter = new XmlWriter(writer);
            Element root = xmlReader.parse(xmlFile);

            writeHeader();
            writeAny();
            writeAnimations(root);
            writeBackgrounds(root);
            writeMusic(root);
            writeSounds(root);
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
        try {
            Element animations = root.getChildByName(ANIMATIONS_NAME);
            simpleType(ANIMATIONS_NAME);
                union(ANY_TYPE);
                    simpleType();
                        restriction(STRING);
                            for (int i = 0; i < animations.getChildCount(); i += 1) {
                                Element child = animations.getChild(i);
                                String text = child.getText();
                                enumeration(text);
                            }
                        xmlWriter.pop();
                    xmlWriter.pop();
                xmlWriter.pop();
            xmlWriter.pop();
            System.out.println("Wrote animations.");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void writeBackgrounds(Element root) {
        writeFilenames(root, BACKGROUNDS_NAME);
        System.out.println("Wrote backgrounds.");
    }

    private static void writeMusic(Element root) {
        writeFilenames(root, MUSIC_NAME);
        System.out.println("Wrote music.");
    }

    private static void writeSounds(Element root) {
        writeFilenames(root, SOUND_NAME);
        System.out.println("Wrote sounds.");
    }

    private static void writeEnd() {
        try {
            xmlWriter.pop();
            System.out.println("Finished writing schema.");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static void writeFilenames(Element root, String type) {
        try {
            Element files = root.getChildByName(type);
            simpleType(type);
                union(ANY_TYPE);
                    simpleType();
                        restriction(STRING);
                            for (int i = 0; i < files.getChildCount(); i += 1) {
                                Element child = files.getChild(i);
                                if (child.getName().equals("text")) {
                                    String text = child.getText();
                                    text = text.replaceAll("\\r", "");
                                    String[] lines = text.split("\\n");
                                    for (String line : lines) {
                                        enumeration(line.trim());
                                    }
                                }
                            }
                        xmlWriter.pop();
                    xmlWriter.pop();
                xmlWriter.pop();
            xmlWriter.pop();
        } catch (IOException io) {
            io.printStackTrace();
        }
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
