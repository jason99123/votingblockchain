package votingBlockchain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import io.netty.util.internal.StringUtil;

public class VotingUser implements User, Serializable{

	private static final long serialVersionUID = 5695080465408336815L;
	private String username;
	private Set<String> roles;
	private String account;
	private String afflication;
	private String org;
	private String enrollmentSecret;
	private String mspID;
	Enrollment enrollment = null;
	private transient VotingStore keyValStore;
	private String keyValStoreName;
	
	public VotingUser(String name, String _org, VotingStore store) {
		this.username = name;
		this.org = _org;
		this.keyValStore = store;
		this.keyValStoreName = tokeyValStoreName(username, _org);
		
		String member = keyValStore.getValue(keyValStoreName);
		if (null != member) {
			saveState();
		} else {
			restoreState();
		}
	}
	public void setAccount(String account) {
		this.account = account;
		saveState();
	}
	
	@Override
	public String getAccount() {
		return this.account;
	}

	public void setAfflication(String aff) {
		this.afflication = aff;
		saveState();
	}
	@Override
	public String getAffiliation() {
		return this.afflication;
	}

	@Override
	public Enrollment getEnrollment() {
		return this.enrollment;
	}
	public void setMspId(String msp) {
		this.mspID = msp;
		saveState();
	}
	@Override
	public String getMspId() {
		return this.mspID;
	}

	@Override
	public String getName() {
		return this.username;
	}
	public void setRoles(Set<String> _roles) {
		this.roles = roles;
		saveState();
	}
	@Override
	public Set<String> getRoles() {
		return this.roles;
	}
	public String getEnrollmentSecret() {
		return this.enrollmentSecret;
	}
	public void setEnrollmentSecret(String secret) {
		this.enrollmentSecret = secret;
		saveState();
	}
	public void setEnrollment(Enrollment enroll) {
		this.enrollment = enroll;
		saveState();
	}
	// check if the username is registered
	public boolean isRegistered() {
		return !StringUtil.isNullOrEmpty(enrollmentSecret);
	}
	// check if the username is enrolled
	public boolean isEnrolled() {
		if (this.enrollment == null) {
			return false;
		} else {
			return true;
		}
	}
	// save user state to io
	public void saveState() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(this);
		oos.flush();
		keyValStore.setValue(keyValStoreName, Hex.toHexString(bos.toByteArray()));
		bos.close();
	}
	
	private VotingUser restoreState() {
		String member = keyValStore.getValue(keyValStoreName);
		if (member != null) {
			byte[] serial = Hex.decode(member);
			ByteArrayInputStream bis = new ByteArrayInputStream(serial);
			ObjectInputStream ois = new ObjectInputStream(bis);
			VotingUser state = (VotingUser) ois.readObject();
			if (state != null) {
				this.username = state.username;
				this.roles = state.roles;
				this.account = state.account;
				this.afflication = state.afflication;
				this.org = state.org;
				this.enrollmentSecret = state.enrollmentSecret;
				this.enrollment = state.enrollment;
				this.mspID = state.mspID;
				return this;
			}
		}
		return null;
	}
	public static String toKeyValstoreName(String name, String org) {
		System.out.println("KEY STORE NAME = "+name+" "+org);
		return "user." +name +org;
	}
}
