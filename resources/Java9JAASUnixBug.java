package de.elbosso.scratch.misc;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Set;

public class Java9JAASUnixBug
{
	public static void main(java.lang.String[] args) throws LoginException
	{
		javax.security.auth.login.LoginContext lc = new javax.security.auth.login.LoginContext("Java9JAASUnixBug");
		lc.login();
		javax.security.auth.Subject subject = lc.getSubject();
		Set<Principal> principals=subject.getPrincipals();
		for(Principal principal:principals)
		{
			if(args.length>0)
				principal.toString();
		}
		PrivilegedAction action = new java.security.PrivilegedAction()
		{
			@Override
			public Object run()
			{
				try
				{
					System.out.println(System.getProperty("java.version"));
					System.out.println(System.getProperty("os.name"));
				}
				catch(java.lang.Throwable t)
				{
					t.printStackTrace();
				}
				return null;
			}
		};
		Subject.doAsPrivileged(subject, action, null);
	}
}
