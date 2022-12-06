module de.jollyday {
    requires java.logging;
    requires jakarta.xml.bind;
    opens de.jollyday.config to jakarta.xml.bind;
    requires java.desktop;

    requires org.threeten.extra;

    exports de.jollyday;
}
