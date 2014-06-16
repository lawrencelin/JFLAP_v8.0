package file.xml;

public abstract class BasicTransducer<T> implements XMLTransducer<T> {
	@Override
	public boolean matchesTag(String tag) {
		return getTag().equals(tag);
	}


}
