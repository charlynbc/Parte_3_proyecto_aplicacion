package dataType;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

// Esta clase es para convertir un Date en un XMLGregorianCalendar que es lo que recibo desde el cliente
public class DateAdapter extends XmlAdapter<XMLGregorianCalendar, Date> {
    @Override
    public Date unmarshal(XMLGregorianCalendar v) {
        return v == null ? null : v.toGregorianCalendar().getTime();
    }

    @Override
    public XMLGregorianCalendar marshal(Date v) {
        if (v == null) {
            return null;
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(v);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}