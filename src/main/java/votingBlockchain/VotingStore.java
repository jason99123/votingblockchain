package votingBlockchain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;

public class VotingStore {
	private static Logger log = Logger.getLogger(VotingStore.class);
	private String storeFile;
	private final Map<String, VotingUser> member = new HashMap<>();
	public VotingStore(File file) {
		this.storeFile = file.getAbsolutePath();
	}
	public void setValue(String key,String value) {
		Properties properties = loadProperties();
		OutputStream out;
		try {
			out = new FileOutputStream(storeFile);
			properties.setProperty(key, value);
			properties.store(out, "");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	private Properties loadProperties() {
		Properties properties = new Properties();
		try {
			InputStream in = new FileInputStream(storeFile);
			properties.load(in);
			in.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return properties;
	}
	public String getValue(String key) {
		Properties properties = loadProperties();
		return properties.getProperty(key);
	}
	public VotingUser getMember(String name, String org) {
		VotingUser user = member.get(VotingUser.toKeyValstoreName(name, org));
		if (user != null) {
			return user;
		} else {
			user = new VotingUser(name, org, this);
			return user;
		}	
	}
	public VotingUser getMember(String name, String org, String mspId, File keyFile, File certFile) {
		VotingUser user = member.get(VotingUser.toKeyValstoreName(name, org));
		if (user != null) {
			log.info("Returning User: "+user);
			return user;
		} else {
			try {
				user = new VotingUser(name, org, this);
				user.setMspId(mspId);
				String cert = new String(IOUtils.toByteArray(new FileInputStream(certFile)), "UTF-8");
				PrivateKey privatekey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(keyFile)));
				user.setEnrollment(new StoreEnrollment(privatekey, cert));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return user;
		}
		
	}
	private PrivateKey getPrivateKeyFromBytes(byte[] byteArray) {
		Reader reader = new StringReader(new String(byteArray));
		PrivateKeyInfo info;
		PEMParser pem = new PEMParser(reader);
		PrivateKey key = null;
		try {
			info = (PrivateKeyInfo) pem.readObject();
			key = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(info);
		} catch (PEMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		return key;
	}
	static {
		try {
			Security.addProvider(new BouncyCastleProvider());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	static class StoreEnrollment implements Enrollment, Serializable{

		private static final long serialVersionUID = 6965341351799577442L;
		private PrivateKey key;
		private String cert;
		
		StoreEnrollment(PrivateKey _key, String _cert){
			this.key=_key;
			this.cert = _cert;
		}
		
		@Override
		public String getCert() {
			return cert;
		}

		@Override
		public PrivateKey getKey() {
			return key;
		}
		
	}
}
