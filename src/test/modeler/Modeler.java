package test.modeler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import test.gui.ModelGUI;
import edu.isi.karma.controller.command.selection.SuperSelectionManager;
import edu.isi.karma.kr2rml.URIFormatter;
import edu.isi.karma.kr2rml.mapping.R2RMLMappingIdentifier;
import edu.isi.karma.kr2rml.writer.KR2RMLRDFWriter;
import edu.isi.karma.kr2rml.writer.N3KR2RMLRDFWriter;
import edu.isi.karma.rdf.GenericRDFGenerator;
import edu.isi.karma.rdf.GenericRDFGenerator.InputType;
import edu.isi.karma.rdf.RDFGeneratorRequest;
import edu.isi.karma.webserver.KarmaException;

public class Modeler {
	
		
	public void convertor() throws URISyntaxException, KarmaException, IOException {

		
		GenericRDFGenerator rdfGenerator = new GenericRDFGenerator(SuperSelectionManager.DEFAULT_SELECTION_TEST_NAME);

		//Construct a R2RMLMappingIdentifier that provides the location of the model and a name for the model
		//You can add multiple models using this API.
		R2RMLMappingIdentifier 	modelIdentifier = new R2RMLMappingIdentifier(
			                "csv", new File( ModelGUI.file.getAbsolutePath()).toURI().toURL());
		rdfGenerator.addModel(modelIdentifier);

		// setting up configuration
		FileWriter output=new FileWriter( ModelGUI.file3.getAbsolutePath());
		String filename =  ModelGUI.file2.getAbsolutePath();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		List<KR2RMLRDFWriter> writers = this.createBasicWriter(pw);
		RDFGeneratorRequest request = new RDFGeneratorRequest("csv", filename);
		request.setInputFile(new File(filename));
		request.setAddProvenance(false);
		request.setDataType(InputType.XML);
		request.addWriters(writers);
		
		rdfGenerator.generateRDF(request);
		
		// writing the RDF 
		output.write(sw.toString());
		output.close();
		
		// RDF generated does not include URI adding manually
		StringBuilder sb = new StringBuilder();
		   
		BufferedReader br = new BufferedReader(new FileReader( ModelGUI.file3.getAbsolutePath()));
		try {
		    String line = br.readLine();
		
              while (line != null) {
            	  // adding the missing URI
            	  String line2=line.replaceAll("<(?![<http]+)","<http://vocab.cs.uni-bonn.de/aml#");            	     	
		        sb.append(line2);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		} finally {
		    br.close();
		}
		output=new FileWriter( ModelGUI.file3.getAbsolutePath());
		output.write(sb.toString());
		output.close();		
	}
	
	protected List<KR2RMLRDFWriter> createBasicWriter(PrintWriter pw) {
		N3KR2RMLRDFWriter writer = new N3KR2RMLRDFWriter(new URIFormatter(), pw);
		List<KR2RMLRDFWriter> writers = new LinkedList<KR2RMLRDFWriter>();
		writers.add(writer);
		return writers;
	}

}


