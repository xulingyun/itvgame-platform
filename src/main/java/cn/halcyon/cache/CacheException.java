package cn.halcyon.cache;

/**
 * ª∫¥Ê“Ï≥£
 * @author Winter Lau
 */
public class CacheException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4634237372338709653L;

	public CacheException(String s) {
		super(s);
	}

	public CacheException(String s, Throwable e) {
		super(s, e);
	}

	public CacheException(Throwable e) {
		super(e);
	}
	
}